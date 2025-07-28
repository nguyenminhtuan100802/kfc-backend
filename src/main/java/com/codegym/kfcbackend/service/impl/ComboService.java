package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ComboItemRequest;
import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.entity.ComboCategory;
import com.codegym.kfcbackend.entity.ProductCategory;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.ComboItem;
import com.codegym.kfcbackend.entity.Product;
import com.codegym.kfcbackend.repository.ComboCategoryRepository;
import com.codegym.kfcbackend.repository.ComboRepository;
import com.codegym.kfcbackend.repository.ProductRepository;
import com.codegym.kfcbackend.service.IComboService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ComboService implements IComboService {
    private final ComboRepository comboRepository;
    private final ProductRepository productRepository;
    private final ComboCategoryRepository comboCategoryRepository;

    public ComboService(ComboRepository comboRepository,
                        ProductRepository productRepository,
                        ComboCategoryRepository comboCategoryRepository
    ) {
        this.comboRepository = comboRepository;
        this.productRepository = productRepository;
        this.comboCategoryRepository = comboCategoryRepository;
    }

    @Override
    @Transactional
    public Combo createCombo(ComboRequest request) {
        if (request.getDescription() == null || request.getDescription().trim().isBlank() ||
                request.getImageUrl() == null || request.getImageUrl().trim().isBlank() ||
                request.getName() == null || request.getName().trim().isBlank() ||
                request.getDiscountStartDate() == null ||
                request.getDiscountEndDate() == null ||
                request.getDiscountStartTime() == null ||
                request.getDiscountEndTime() == null ||
                request.getDiscountAmount() == null || request.getDiscountAmount().compareTo(BigDecimal.ZERO) < 0 ||
                request.getComboItems().isEmpty()
        ) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }

        LocalDate startDate = request.getDiscountStartDate();
        LocalDate endDate = request.getDiscountEndDate();
        LocalTime startTime = request.getDiscountStartTime();
        LocalTime endTime = request.getDiscountEndTime();

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(String.format(AppConstants.COMBO_DATE_ERROR, startDate, endDate));
        }
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException(String.format(AppConstants.COMBO_TIME_ERROR, startTime, endTime));
        }

        ComboCategory existingComboCategory = comboCategoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Combo existingCombo = comboRepository.findByName(request.getName()).orElse(null);
        if (existingCombo != null) {
            throw new RuntimeException(String.format(AppConstants.COMBO_ALREADY_EXISTS, request.getName()));
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        Combo newCombo = new Combo();
        List<ComboItem> comboItems = new ArrayList<>();
        for (ComboItemRequest comboItemRequest : request.getComboItems()) {
            if (comboItemRequest.getQuantity() <= 0) {
                throw new RuntimeException(AppConstants.PRODUCT_QUANTITY_ERROR);
            }
            Product existingProduct = productRepository.findByName(comboItemRequest.getProductName())
                    .orElseThrow(() -> new RuntimeException(String.format(
                            AppConstants.PRODUCT_NOT_FOUND, comboItemRequest.getProductName())));

            BigDecimal quantity = BigDecimal.valueOf(comboItemRequest.getQuantity());
            BigDecimal itemTotalPrice = existingProduct.getPrice().multiply(quantity);
            totalPrice = totalPrice.add(itemTotalPrice);

            ComboItem comboItem = ComboItem.builder()
                    .product(existingProduct)
                    .combo(newCombo)
                    .quantity(comboItemRequest.getQuantity())
                    .build();
            comboItems.add(comboItem);
        }

        if (request.getDiscountAmount().compareTo(totalPrice) > 0) {
            throw new RuntimeException(AppConstants.DISCOUNT_ERROR);
        }

        newCombo.setName(request.getName());
        newCombo.setDescription(request.getDescription());
        newCombo.setImageUrl(request.getImageUrl());
        newCombo.setTotalPrice(totalPrice);
        newCombo.setDiscountAmount(request.getDiscountAmount());
        newCombo.setTotalPriceAfterDiscount(totalPrice.subtract(request.getDiscountAmount()));
        newCombo.setDiscountStartDate(startDate);
        newCombo.setDiscountEndDate(endDate);
        newCombo.setDiscountStartTime(startTime);
        newCombo.setDiscountEndTime(endTime);
        newCombo.setComboCategory(existingComboCategory);
        newCombo.setComboItems(comboItems);

        Combo savedCombo = comboRepository.save(newCombo);
        return savedCombo;
    }

    @Override
    public List<Combo> getAllCombos() {
        List<Combo> combos = comboRepository.findAll();
        return combos;
    }

    @Override
    public Combo editCombo(Long id, ComboRequest request) {
        if (request.getDescription() == null || request.getDescription().trim().isBlank() ||
                request.getImageUrl() == null || request.getImageUrl().trim().isBlank() ||
                request.getName() == null || request.getName().trim().isBlank() ||
                request.getDiscountStartDate() == null ||
                request.getDiscountEndDate() == null ||
                request.getDiscountStartTime() == null ||
                request.getDiscountEndTime() == null ||
                request.getDiscountAmount() == null || request.getDiscountAmount().compareTo(BigDecimal.ZERO) < 0 ||
                request.getComboItems().isEmpty()
        ) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }

        LocalDate startDate = request.getDiscountStartDate();
        LocalDate endDate = request.getDiscountEndDate();
        LocalTime startTime = request.getDiscountStartTime();
        LocalTime endTime = request.getDiscountEndTime();

        if (startDate.isAfter(endDate)) {
            throw new RuntimeException(String.format(AppConstants.COMBO_DATE_ERROR, startDate, endDate));
        }
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException(String.format(AppConstants.COMBO_TIME_ERROR, startTime, endTime));
        }
        Combo checkExistingComboWithName = comboRepository.findByName(request.getName()).orElse(null);
        if (checkExistingComboWithName != null && !checkExistingComboWithName.getId().equals(id)) {
            throw new RuntimeException(String.format(AppConstants.COMBO_ALREADY_EXISTS, request.getName()));
        }

        ComboCategory existingComboCategory = comboCategoryRepository.findByName(request.getCategoryName())
                .orElseThrow(() -> new RuntimeException(
                        String.format(AppConstants.CATEGORY_NOT_FOUND,
                                request.getCategoryName())));

        Combo existingCombo = comboRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("Combo not found with id %d", id)));

        existingCombo.getComboItems().clear();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (ComboItemRequest comboItemRequest : request.getComboItems()) {
            if (comboItemRequest.getQuantity() <= 0) {
                throw new RuntimeException(AppConstants.PRODUCT_QUANTITY_ERROR);
            }
            Product existingProduct = productRepository.findByName(comboItemRequest.getProductName())
                    .orElseThrow(() -> new RuntimeException(String.format(
                            AppConstants.PRODUCT_NOT_FOUND, comboItemRequest.getProductName())));

            BigDecimal quantity = BigDecimal.valueOf(comboItemRequest.getQuantity());
            BigDecimal itemTotalPrice = existingProduct.getPrice().multiply(quantity);
            totalPrice = totalPrice.add(itemTotalPrice);

            ComboItem comboItem = ComboItem.builder()
                    .product(existingProduct)
                    .combo(existingCombo)
                    .quantity(comboItemRequest.getQuantity())
                    .build();
            existingCombo.getComboItems().add(comboItem);
        }

        if (request.getDiscountAmount().compareTo(totalPrice) > 0) {
            throw new RuntimeException(AppConstants.DISCOUNT_ERROR);
        }

        existingCombo.setName(request.getName());
        existingCombo.setDescription(request.getDescription());
        existingCombo.setImageUrl(request.getImageUrl());
        existingCombo.setTotalPrice(totalPrice);
        existingCombo.setDiscountAmount(request.getDiscountAmount());
        existingCombo.setTotalPriceAfterDiscount(totalPrice.subtract(request.getDiscountAmount()));
        existingCombo.setDiscountStartDate(startDate);
        existingCombo.setDiscountEndDate(endDate);
        existingCombo.setDiscountStartTime(startTime);
        existingCombo.setDiscountEndTime(endTime);
        existingCombo.setComboCategory(existingComboCategory);
        Combo savedCombo = comboRepository.save(existingCombo);
        return savedCombo;
    }

    @Override
    public void deleteCombo(Long id) {
        Combo existingCombo = comboRepository.findById(id).orElseThrow(() -> new RuntimeException("Combo not found with id " + id));
        comboRepository.delete(existingCombo);
    }
}
