package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.BillItemRequest;
import com.codegym.kfcbackend.dto.request.BillRequest;
import com.codegym.kfcbackend.dto.request.SummaryReportRequest;
import com.codegym.kfcbackend.dto.response.SummaryReportResponse;
import com.codegym.kfcbackend.dto.response.BillResponse;
import com.codegym.kfcbackend.entity.Bill;
import com.codegym.kfcbackend.entity.BillItem;
import com.codegym.kfcbackend.entity.BillItemDetail;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.ComboItem;
import com.codegym.kfcbackend.entity.Ingredient;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.entity.RecipeItem;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.enums.BillStatus;
import com.codegym.kfcbackend.repository.BillRepository;
import com.codegym.kfcbackend.repository.ComboRepository;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.repository.UserRepository;
import com.codegym.kfcbackend.service.IBillService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BillService implements IBillService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ComboRepository comboRepository;
    private final IngredientRepository ingredientRepository;
    private final BillRepository billRepository;

    public BillService(UserRepository userRepository,
                       ProductRepository productRepository,
                       ComboRepository comboRepository,
                       IngredientRepository ingredientRepository,
                       BillRepository billRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.comboRepository = comboRepository;
        this.ingredientRepository = ingredientRepository;
        this.billRepository = billRepository;
    }

    @Override
    @Transactional
    public Bill createBill(BillRequest request) {
        if (request.getStaffName() == null || request.getStaffName().trim().isBlank() ||
                request.getBillItems().isEmpty()
        ) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        User existingUser = userRepository.findByUsername(request.getStaffName())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.getStaffName()));

        Bill bill = Bill.builder()
                .staff(existingUser)
                .billDate(LocalDateTime.now())
                .billItems(new ArrayList<>())
                .totalRevenue(BigDecimal.ZERO)
                .totalCost(BigDecimal.ZERO)
                .build();
        for (BillItemRequest billItemRequest : request.getBillItems()) {
            if (billItemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Bill item quantity must be > 0");
            }
            BillItem billItem = BillItem.builder()
                    .quantity(billItemRequest.getQuantity())
                    .billItemDetails(new ArrayList<>())
                    .totalCost(BigDecimal.ZERO)
                    .bill(bill)
                    .build();
            // Kiểm tra xem là PRODUCT hay COMBO
            Product existingProduct = productRepository.findByName(billItemRequest.getComboNameOrProductName()).orElse(null);
            if (existingProduct != null) {
                billItem.setProduct(existingProduct);
                billItem.setCombo(null);
                billItem.setTotalPrice(existingProduct.getPrice().multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));

                for (RecipeItem recipeItem : existingProduct.getRecipeItems()) {
                    Ingredient existingIngredient = ingredientRepository.findByName(recipeItem.getIngredient().getName()).orElseThrow(
                            () -> new RuntimeException("Ingredient not found: " + recipeItem.getIngredient().getName()));
                    BigDecimal usedQuantity = BigDecimal.valueOf(billItemRequest.getQuantity())
                            .multiply(recipeItem.getQuantity());
                    if (existingIngredient.getCurrentQuantity().compareTo(usedQuantity) < 0) {
                        throw new RuntimeException("Không đủ kho cho nguyên liệu " + existingIngredient.getName());
                    }
                    existingIngredient.setCurrentQuantity(existingIngredient.getCurrentQuantity().subtract(usedQuantity));

                    BillItemDetail billItemDetail = BillItemDetail.builder()
                            .ingredientName(existingIngredient.getName())
                            .unitCost(existingIngredient.getAverageCost())
                            .totalCost(existingIngredient.getAverageCost().multiply(usedQuantity))
                            .usedQuantity(usedQuantity)
                            .baseUnitCode(existingIngredient.getBaseUnitCode())
                            .billItem(billItem)
                            .build();
                    billItem.setTotalCost(billItem.getTotalCost().add(billItemDetail.getTotalCost()));
                    billItem.getBillItemDetails().add(billItemDetail);
                }
            } else {
                Combo existingCombo = comboRepository.findByName(billItemRequest.getComboNameOrProductName()).orElse(null);
                if (existingCombo != null) {
                    billItem.setCombo(existingCombo);
                    billItem.setProduct(null);

                    LocalDateTime billTime = bill.getBillDate();
                    LocalDate discountStartDate = existingCombo.getDiscountStartDate();
                    LocalDate discountEndDate = existingCombo.getDiscountEndDate();
                    LocalTime discountStartTime = existingCombo.getDiscountStartTime();
                    LocalTime discountEndTime = existingCombo.getDiscountEndTime();

                    boolean sameMonthAndYear =
                            billTime.getYear() == discountStartDate.getYear() &&
                                    billTime.getMonth() == discountStartDate.getMonth() &&
                                    (!billTime.toLocalDate().isBefore(discountStartDate) && !billTime.toLocalDate().isAfter(discountEndDate));
                    if (sameMonthAndYear) {
                        LocalTime billTimeOnly = billTime.toLocalTime();
                        if (!billTimeOnly.isBefore(discountStartTime) && !billTimeOnly.isAfter(discountEndTime)) {
                            billItem.setTotalPrice(existingCombo.getTotalPriceAfterDiscount().multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));
                        } else {
                            billItem.setTotalPrice(existingCombo.getTotalPrice().multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));
                        }
                    } else {
                        billItem.setTotalPrice(existingCombo.getTotalPrice().multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));
                    }

                    for (ComboItem comboItem : existingCombo.getComboItems()) {
                        existingProduct = productRepository.findByName(comboItem.getProduct().getName())
                                .orElseThrow(() -> new RuntimeException("Product not found: " + comboItem.getProduct().getName()));
                        for (RecipeItem recipeItem : existingProduct.getRecipeItems()) {
                            Ingredient existingIngredient = ingredientRepository.findByName(recipeItem.getIngredient().getName()).orElseThrow(
                                    () -> new RuntimeException("Ingredient not found: " + recipeItem.getIngredient().getName()));

                            BigDecimal usedQuantity = BigDecimal.valueOf(billItemRequest.getQuantity())
                                    .multiply(BigDecimal.valueOf(comboItem.getQuantity()))
                                    .multiply(recipeItem.getQuantity());

                            if (existingIngredient.getCurrentQuantity().compareTo(usedQuantity) < 0) {
                                throw new RuntimeException("Không đủ kho cho nguyên liệu " + existingIngredient.getName());
                            }
                            existingIngredient.setCurrentQuantity(existingIngredient.getCurrentQuantity().subtract(usedQuantity));

                            BillItemDetail billItemDetail = BillItemDetail.builder()
                                    .ingredientName(existingIngredient.getName())
                                    .unitCost(existingIngredient.getAverageCost())
                                    .usedQuantity(usedQuantity)
                                    .totalCost(existingIngredient.getAverageCost().multiply(usedQuantity))
                                    .baseUnitCode(existingIngredient.getBaseUnitCode())
                                    .billItem(billItem)
                                    .build();
                            billItem.getBillItemDetails().add(billItemDetail);
                            billItem.setTotalCost(billItem.getTotalCost().add(billItemDetail.getTotalCost()));
                        }
                    }
                } else {
                    throw new RuntimeException("Combo name or product name not found: " + billItemRequest.getComboNameOrProductName());
                }
            }
            bill.setTotalRevenue(bill.getTotalRevenue().add(billItem.getTotalPrice()));
            bill.setTotalCost(bill.getTotalCost().add(billItem.getTotalCost()));
            bill.getBillItems().add(billItem);
        }
        bill.setStatus(BillStatus.PAID);
        Bill savedBill = billRepository.save(bill);
        return savedBill;
    }

    @Override
    public List<Bill> getAllBills() {
        List<Bill> bills = billRepository.findAllBillsOrderByDateDesc();
        return bills;
    }

    @Override
    public SummaryReportResponse getBillsBetween(SummaryReportRequest request) {
        if (request.getFromDate() == null || request.getToDate() == null) {
            return null;
        }
        if (!request.getFromDate().isBefore(request.getToDate())) {
            throw new RuntimeException("fromDate must be before toDate");
        }
        List<Bill> bills = billRepository.findAllBillsOrderByDateDesc();
        SummaryReportResponse summaryReportResponse = SummaryReportResponse.builder()
                .totalRevenue(BigDecimal.ZERO)
                .totalCost(BigDecimal.ZERO)
                .totalProfit(BigDecimal.ZERO)
                .totalBills(0L)
                .bills(new ArrayList<>())
                .build();
        for (Bill bill : bills) {
            if ((bill.getBillDate().toLocalDate().isEqual(request.getFromDate()) || bill.getBillDate().toLocalDate().isAfter(request.getFromDate())) &&
                    (bill.getBillDate().toLocalDate().isEqual(request.getToDate()) || bill.getBillDate().toLocalDate().isBefore(request.getToDate())) &&
                    bill.getStatus() == BillStatus.PAID) {
                BillResponse billResponse = BillResponse.builder()
                        .billDate(bill.getBillDate())
                        .staffName(bill.getStaff().getUsername())
                        .totalPrice(bill.getTotalRevenue())
                        .totalCost(bill.getTotalCost())
                        .status(bill.getStatus())
                        .build();
                summaryReportResponse.getBills().add(billResponse);
                summaryReportResponse.setTotalRevenue(summaryReportResponse.getTotalRevenue().add(bill.getTotalRevenue()));
                summaryReportResponse.setTotalCost(summaryReportResponse.getTotalCost().add(bill.getTotalCost()));
                summaryReportResponse.setTotalBills(summaryReportResponse.getTotalBills() + 1);
            }
        }
        summaryReportResponse.setTotalProfit(summaryReportResponse.getTotalRevenue().subtract(summaryReportResponse.getTotalCost()));

        return summaryReportResponse;
    }

    @Override
    @Transactional
    public void cancelBill(Long id) {
        Bill existingBill = billRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Không tìm thấy bill với id: " + id));

        if (existingBill.getStatus() == BillStatus.CANCELED) {
            throw new RuntimeException("Bill đã bị hủy trước đó.");
        }

        for (BillItem billItem : existingBill.getBillItems()) {
            for (BillItemDetail detail : billItem.getBillItemDetails()) {
                Ingredient ingredient = ingredientRepository.findByName(detail.getIngredientName())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu: " + detail.getIngredientName()));

                ingredient.setCurrentQuantity(ingredient.getCurrentQuantity().add(detail.getUsedQuantity()));
            }
        }
        existingBill.setStatus(BillStatus.CANCELED);
        Bill savedBill = billRepository.save(existingBill);
    }
}
