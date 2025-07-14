package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.UnitOfMeasureRequest;
import com.codegym.kfcbackend.entity.UnitOfMeasure;

import java.util.List;

public interface IUnitOfMeasureService {
    UnitOfMeasure createUnitOfMeasure(UnitOfMeasureRequest request);
    List<UnitOfMeasure> getAllUnitOfMeasures();
    UnitOfMeasure editUnitOfMeasure(Long id, UnitOfMeasureRequest request);
    void deleteUnitOfMeasure(Long id);
}
