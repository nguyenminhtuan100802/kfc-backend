package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.UnitOfMeasureRequest;
import com.codegym.kfcbackend.dto.response.PageCacheResponse;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.exception.BusinessException;
import com.codegym.kfcbackend.repository.UnitOfMeasureRepository;
import com.codegym.kfcbackend.service.IUnitOfMeasureService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UnitOfMeasureService implements IUnitOfMeasureService {
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public UnitOfMeasureService(UnitOfMeasureRepository unitOfMeasureRepository) {
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @CacheEvict(value = "unitOfMeasures", allEntries = true)
    public UnitOfMeasure createUnitOfMeasure(UnitOfMeasureRequest request) {
        String code = request.getCode().toLowerCase().trim();
        String baseUnitCode = request.getBaseUnitCode().toLowerCase().trim();
        if (code.equals(baseUnitCode)) {
            throw new BusinessException(AppConstants.UNIT_OF_MEASURE_ERROR);
        }

        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findByCodename(code).orElse(null);
        if (existingUnitOfMeasure != null) {
            throw new BusinessException(String.format(AppConstants.UNIT_OF_MEASURE_ALREADY_EXISTS, code));
        }

        UnitOfMeasure unitOfMeasure = UnitOfMeasure.builder()
                .code(code)
                .baseUnitCode(baseUnitCode)
                .factorToBase(request.getFactorToBase())
                .createdBy(SecurityContextHolder.getContext().getAuthentication().getName())
                .build();
        UnitOfMeasure savedUnitOfMeasure = unitOfMeasureRepository.save(unitOfMeasure);
        return savedUnitOfMeasure;
    }

    @Override
    @Cacheable(value = "unitOfMeasures", key = "'page-' + #page + '-' + #size + '-' + #keyword")
    public PageCacheResponse<UnitOfMeasure> getUnitsByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UnitOfMeasure> unitOfMeasures = unitOfMeasureRepository.findAllByKeyword(keyword, pageable);
        PageCacheResponse<UnitOfMeasure> pageCacheResponse = PageCacheResponse.<UnitOfMeasure>builder()
                .content(unitOfMeasures.getContent())
                .totalElements(unitOfMeasures.getTotalElements())
                .totalPages(unitOfMeasures.getTotalPages())
                .build();
        return pageCacheResponse;
    }

    @Override
    public List<UnitOfMeasure> getAllUnits() {
        List<UnitOfMeasure> unitOfMeasures = unitOfMeasureRepository.findAllGroupByBaseUnitThenFactorDesc();
        return unitOfMeasures;
    }

    @Override
    @Transactional
    @CacheEvict(value = "unitOfMeasures", allEntries = true)
    public UnitOfMeasure editUnitOfMeasure(Long id, UnitOfMeasureRequest request) {
        String code = request.getCode().toLowerCase().trim();
        String baseUnitCode = request.getBaseUnitCode().toLowerCase().trim();
        if (code.equals(baseUnitCode)) {
            throw new BusinessException(AppConstants.UNIT_OF_MEASURE_ERROR);
        }

        UnitOfMeasure updateUnitOfMeasure = unitOfMeasureRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND_WITH_ID, id)));

        if (!updateUnitOfMeasure.getId().equals(id)) {
            throw new BusinessException(String.format(AppConstants.UNIT_OF_MEASURE_ALREADY_EXISTS, code));
        }

        updateUnitOfMeasure.setCode(code);
        updateUnitOfMeasure.setBaseUnitCode(request.getBaseUnitCode().toLowerCase());
        updateUnitOfMeasure.setFactorToBase(request.getFactorToBase());
        updateUnitOfMeasure.setModifiedBy(SecurityContextHolder.getContext().getAuthentication().getName());

        return updateUnitOfMeasure;
    }

    @Override
    @CacheEvict(value = "unitOfMeasures", allEntries = true)
    public void deleteUnitOfMeasure(Long id) {
        UnitOfMeasure existingUnitOfMeasure = unitOfMeasureRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format(AppConstants.UNIT_OF_MEASURE_NOT_FOUND_WITH_ID, id)));
        unitOfMeasureRepository.delete(existingUnitOfMeasure);
    }
}
