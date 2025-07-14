package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.UnitOfMeasureRequest;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IUnitOfMeasureService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UnitOfMeasureService implements IUnitOfMeasureService {
    private UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public UnitOfMeasure createUnitOfMeasure(UnitOfMeasureRequest request) {
        if (request.getCode().isBlank() || request.getBaseUnitCode().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        if (request.getFactorToBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(AppConstants.FACTOR_TO_BASE_ERROR);
        }
        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByCodename(request.getCode().toLowerCase()).orElse(null);
        if (existingUnitOfMeasure != null) {
            throw new RuntimeException(String.format(AppConstants.UNIT_OF_MEASURE_ALREADY_EXISTS, request.getCode()));
        }
        if (request.getCode().toLowerCase().equals(request.getBaseUnitCode().toLowerCase())) {
            throw new RuntimeException(AppConstants.UNIT_OF_MEASURE_ERROR);
        }
        UnitOfMeasure unitOfMeasure = UnitOfMeasure.builder()
                .code(request.getCode().toLowerCase())
                .baseUnitCode(request.getBaseUnitCode().toLowerCase())
                .factorToBase(request.getFactorToBase())
                .build();
        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);
        return savedUnitOfMeasure;
    }

    @Override
    public List<UnitOfMeasure> getAllUnitOfMeasures() {
        List<UnitOfMeasure> unitOfMeasures = unitOfMeasureRepository.findAllGroupByBaseUnitThenFactorDesc();
        return unitOfMeasures;
    }

    @Override
    public UnitOfMeasure editUnitOfMeasure(Long id, UnitOfMeasureRequest request) {
        if (request.getCode().isBlank() || request.getBaseUnitCode().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        if (request.getCode().toLowerCase().equals(request.getBaseUnitCode().toLowerCase())) {
            throw new RuntimeException(AppConstants.UNIT_OF_MEASURE_ERROR);
        }
        if (request.getFactorToBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(AppConstants.FACTOR_TO_BASE_ERROR);
        }
        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByCodename(request.getCode().toLowerCase()).orElse(null);
        if (existingUnitOfMeasure != null && !existingUnitOfMeasure.getId().equals(id)) {
            throw new RuntimeException(String.format(AppConstants.UNIT_OF_MEASURE_ALREADY_EXISTS, request.getCode()));
        }

        UnitOfMeasure updateUnitOfMeasure = unitOfMeasureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND_WITH_ID, id)));

        updateUnitOfMeasure.setCode(request.getCode().toLowerCase());
        updateUnitOfMeasure.setBaseUnitCode(request.getBaseUnitCode().toLowerCase());
        updateUnitOfMeasure.setFactorToBase(request.getFactorToBase());

        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(updateUnitOfMeasure);
        return savedUnitOfMeasure;
    }

    @Override
    public void deleteUnitOfMeasure(Long id) {
        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND_WITH_ID, id)));
        unitOfMeasureRepository.delete(existingUnitOfMeasure);
    }
}
