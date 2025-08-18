package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.BillItemRequest;
import com.codegym.kfcbackend.dto.request.BillRequest;
import com.codegym.kfcbackend.dto.request.SummaryReportRequest;
import com.codegym.kfcbackend.dto.response.IngredientUsedSummaryResponse;
import com.codegym.kfcbackend.dto.response.ProductSaleSummaryResponse;
import com.codegym.kfcbackend.dto.response.StaffSaleSummaryResponse;
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
import com.codegym.kfcbackend.repository.BillItemDetailRepository;
import com.codegym.kfcbackend.repository.BillItemRepository;
import com.codegym.kfcbackend.repository.BillRepository;
import com.codegym.kfcbackend.repository.ComboItemRepository;
import com.codegym.kfcbackend.repository.ComboRepository;
import com.codegym.kfcbackend.repository.IngredientRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.repository.RecipeItemRepository;
import com.codegym.kfcbackend.repository.UserRepository;
import com.codegym.kfcbackend.service.IBillService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BillService implements IBillService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ComboRepository comboRepository;
    private final ComboItemRepository comboItemRepository;
    private final IngredientRepository ingredientRepository;
    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final BillItemDetailRepository billItemDetailRepository;
    private final RecipeItemRepository recipeItemRepository;

    public BillService(UserRepository userRepository,
                       ProductRepository productRepository,
                       ComboRepository comboRepository,
                       ComboItemRepository comboItemRepository,
                       IngredientRepository ingredientRepository,
                       BillRepository billRepository,
                       BillItemRepository billItemRepository,
                       BillItemDetailRepository billItemDetailRepository,
                       RecipeItemRepository recipeItemRepository) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.comboRepository = comboRepository;
        this.comboItemRepository = comboItemRepository;
        this.ingredientRepository = ingredientRepository;
        this.billRepository = billRepository;
        this.billItemRepository = billItemRepository;
        this.billItemDetailRepository = billItemDetailRepository;
        this.recipeItemRepository = recipeItemRepository;
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

        Bill savedBill = billRepository.save(Bill.builder()
                .staff(existingUser)
                .billDate(LocalDateTime.now())
                .totalRevenue(BigDecimal.ZERO)
                .totalCost(BigDecimal.ZERO)
                .build());

        for (BillItemRequest billItemRequest : request.getBillItems()) {
            if (billItemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Bill item quantity must be > 0");
            }

            BillItem savedBillItem = billItemRepository.save(BillItem.builder()
                    .quantity(billItemRequest.getQuantity())
                    .totalCost(BigDecimal.ZERO)
                    .bill(savedBill)
                    .build());

            // Kiểm tra xem là PRODUCT hay COMBO
            Product existingProduct = productRepository.findByName(billItemRequest.getComboNameOrProductName()).orElse(null);
            if (existingProduct != null) {
                savedBillItem.setProduct(existingProduct);
                savedBillItem.setCombo(null);
                savedBillItem.setTotalPrice(existingProduct.getPrice().multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));

                List<RecipeItem> recipeItems = recipeItemRepository.findAllByProductId(existingProduct.getId());
                for (RecipeItem recipeItem : recipeItems) {
                    Ingredient existingIngredient = ingredientRepository.findByName(recipeItem.getIngredient().getName()).orElseThrow(
                            () -> new RuntimeException("Ingredient not found: " + recipeItem.getIngredient().getName()));

                    BigDecimal usedQuantity = BigDecimal.valueOf(billItemRequest.getQuantity())
                            .multiply(recipeItem.getQuantity());
                    if (existingIngredient.getCurrentQuantity().compareTo(usedQuantity) < 0) {
                        throw new RuntimeException("Không đủ kho cho nguyên liệu " + existingIngredient.getName());
                    }
                    existingIngredient.setCurrentQuantity(existingIngredient.getCurrentQuantity().subtract(usedQuantity));

                    BillItemDetail savedBillItemDetail = billItemDetailRepository.save(BillItemDetail.builder()
                            .ingredientName(existingIngredient.getName())
                            .unitCost(existingIngredient.getAverageCost())
                            .totalCost(existingIngredient.getAverageCost().multiply(usedQuantity))
                            .usedQuantity(usedQuantity)
                            .baseUnitCode(existingIngredient.getBaseUnitCode())
                            .billItem(savedBillItem)
                            .build());
                    savedBillItem.setTotalCost(savedBillItem.getTotalCost().add(savedBillItemDetail.getTotalCost()));
                }
            } else {
                Combo existingCombo = comboRepository.findByName(billItemRequest.getComboNameOrProductName()).orElse(null);
                if (existingCombo != null) {
                    savedBillItem.setCombo(existingCombo);
                    savedBillItem.setProduct(null);

                    LocalDateTime billTime = savedBill.getBillDate();
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
                            savedBillItem.setTotalPrice(existingCombo.getTotalPriceAfterDiscount()
                                    .multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));
                        }
                    } else {
                        savedBillItem.setTotalPrice(existingCombo.getTotalPrice()
                                .multiply(BigDecimal.valueOf(billItemRequest.getQuantity())));
                    }

                    List<ComboItem> comboItems = comboItemRepository.findAllByComboId(existingCombo.getId());
                    for (ComboItem comboItem : comboItems) {
                        existingProduct = productRepository.findByName(comboItem.getProduct().getName())
                                .orElseThrow(() -> new RuntimeException("Product not found: " + comboItem.getProduct().getName()));

                        List<RecipeItem> recipeItems = recipeItemRepository.findAllByProductId(existingProduct.getId());
                        for (RecipeItem recipeItem : recipeItems) {
                            Ingredient existingIngredient = ingredientRepository.findByName(recipeItem.getIngredient().getName()).orElseThrow(
                                    () -> new RuntimeException("Ingredient not found: " + recipeItem.getIngredient().getName()));

                            BigDecimal usedQuantity = BigDecimal.valueOf(billItemRequest.getQuantity())
                                    .multiply(BigDecimal.valueOf(comboItem.getQuantity()))
                                    .multiply(recipeItem.getQuantity());

                            if (existingIngredient.getCurrentQuantity().compareTo(usedQuantity) < 0) {
                                throw new RuntimeException("Không đủ kho cho nguyên liệu " + existingIngredient.getName());
                            }
                            existingIngredient.setCurrentQuantity(existingIngredient.getCurrentQuantity().subtract(usedQuantity));

                            BillItemDetail savedBillItemDetail = billItemDetailRepository.save(BillItemDetail.builder()
                                    .ingredientName(existingIngredient.getName())
                                    .unitCost(existingIngredient.getAverageCost())
                                    .usedQuantity(usedQuantity)
                                    .totalCost(existingIngredient.getAverageCost().multiply(usedQuantity))
                                    .baseUnitCode(existingIngredient.getBaseUnitCode())
                                    .billItem(savedBillItem)
                                    .build());
                            savedBillItem.setTotalCost(savedBillItem.getTotalCost().add(savedBillItemDetail.getTotalCost()));
                        }
                    }
                } else {
                    throw new RuntimeException("Combo name or product name not found: " + billItemRequest.getComboNameOrProductName());
                }
            }
            savedBill.setTotalRevenue(savedBill.getTotalRevenue().add(savedBillItem.getTotalPrice()));
            savedBill.setTotalCost(savedBill.getTotalCost().add(savedBillItem.getTotalCost()));
        }
        savedBill.setStatus(BillStatus.PAID);
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
                .staffSalesSummaries(new ArrayList<>())
                .productSalesSummaries(new ArrayList<>())
                .ingredientUsedSummaries(new ArrayList<>())
                .build();

        for (Bill bill : bills) {
            if ((bill.getBillDate().toLocalDate().isEqual(request.getFromDate()) || bill.getBillDate().toLocalDate().isAfter(request.getFromDate())) &&
                    (bill.getBillDate().toLocalDate().isEqual(request.getToDate()) || bill.getBillDate().toLocalDate().isBefore(request.getToDate())) &&
                    bill.getStatus() == BillStatus.PAID) {
//                staff sale
                boolean isNewStaff = true;
                for (StaffSaleSummaryResponse staff : summaryReportResponse.getStaffSalesSummaries()) {
                    if (staff.getUsername().equals(bill.getStaff().getUsername())) {
                        staff.setTotalBillSold(staff.getTotalBillSold() + 1);
                        isNewStaff = false;
                        break;
                    }
                }
                if (isNewStaff) {
                    summaryReportResponse.getStaffSalesSummaries().add(
                            StaffSaleSummaryResponse.builder()
                                    .staffName(bill.getStaff().getFullName())
                                    .username(bill.getStaff().getUsername())
                                    .roleName(bill.getStaff().getUserRoles().get(0).getRole().getName())
                                    .totalBillSold(1L)
                                    .build()
                    );
                }
//                product sale
                List<BillItem> billItems = billItemRepository.findAllByBillId(bill.getId());
                for (BillItem billItem : billItems) {
                    boolean isNewProductOrCombo = true;
                    String productNameOrComboName = null;
                    String productCategoryNameOrComboCategoryName = null;

                    if (billItem.getProduct() != null) {
                        productNameOrComboName = billItem.getProduct().getName();
                        productCategoryNameOrComboCategoryName = billItem.getProduct().getProductCategory().getName();
                    } else if (billItem.getCombo() != null) {
                        productNameOrComboName = billItem.getCombo().getName();
                        productCategoryNameOrComboCategoryName = billItem.getCombo().getComboCategory().getName();
                    }

                    for (ProductSaleSummaryResponse product : summaryReportResponse.getProductSalesSummaries()) {
                        if (product.getProductNameOrComboName().equals(productNameOrComboName)) {
                            product.setTotalSold(product.getTotalSold() + billItem.getQuantity());
                            isNewProductOrCombo = false;
                            break;
                        }
                    }
                    if (isNewProductOrCombo) {
                        summaryReportResponse.getProductSalesSummaries().add(
                                ProductSaleSummaryResponse.builder()
                                        .productNameOrComboName(productNameOrComboName)
                                        .productCategoryNameOrComboCategoryName(productCategoryNameOrComboCategoryName)
                                        .totalSold(Long.valueOf(billItem.getQuantity()))
                                        .build()
                        );
                    }
                }

//                used ingredient
                for (BillItem billItem : billItems) {
                    List<BillItemDetail> billItemDetails = billItemDetailRepository.findAllByBillItemId(billItem.getId());
                    for (BillItemDetail billItemDetail : billItemDetails) {
                        boolean isNewIngredient = true;
                        for (IngredientUsedSummaryResponse ingredient : summaryReportResponse.getIngredientUsedSummaries()) {
                            if (billItemDetail.getIngredientName().equals(ingredient.getIngredientName())) {
                                ingredient.setQuantityUsed(ingredient.getQuantityUsed().add(billItemDetail.getUsedQuantity()));
                                isNewIngredient = false;
                                break;
                            }
                        }
                        if (isNewIngredient) {
                            Ingredient existingIngredient = ingredientRepository.findByName(billItemDetail.getIngredientName())
                                    .orElseThrow(() -> new RuntimeException("Ingredient not found: " + billItemDetail.getIngredientName()));

                            summaryReportResponse.getIngredientUsedSummaries().add(
                                    IngredientUsedSummaryResponse.builder()
                                            .ingredientName(billItemDetail.getIngredientName())
                                            .ingredientCategoryName(existingIngredient.getIngredientCategory().getName())
                                            .quantityUsed(billItemDetail.getUsedQuantity())
                                            .quantityRemaining(existingIngredient.getCurrentQuantity())
                                            .baseUnitCode(billItemDetail.getBaseUnitCode())
                                            .build()
                            );
                        }
                    }
                }
//                bill between
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

//              sort
        summaryReportResponse.getStaffSalesSummaries().sort(
                Comparator.comparingLong(StaffSaleSummaryResponse::getTotalBillSold)
                        .reversed()
        );
        summaryReportResponse.getProductSalesSummaries().sort(
                Comparator.comparingLong(ProductSaleSummaryResponse::getTotalSold)
                        .reversed()
        );
        summaryReportResponse.getIngredientUsedSummaries().sort(
                Comparator.comparing(IngredientUsedSummaryResponse::getQuantityUsed)
                        .reversed()
        );
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

        List<BillItem> billItems = billItemRepository.findAllByBillId(existingBill.getId());
        for (BillItem billItem : billItems) {
            List<BillItemDetail> billItemDetails = billItemDetailRepository.findAllByBillItemId(billItem.getId());
            for (BillItemDetail detail : billItemDetails) {
                Ingredient ingredient = ingredientRepository.findByName(detail.getIngredientName())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy nguyên liệu: " + detail.getIngredientName()));

                ingredient.setCurrentQuantity(ingredient.getCurrentQuantity().add(detail.getUsedQuantity()));
            }
        }
        existingBill.setStatus(BillStatus.CANCELED);
        Bill savedBill = billRepository.save(existingBill);
    }
}
