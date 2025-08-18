package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.UnitOfMeasureRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.PageCacheResponse;
import com.codegym.kfcbackend.dto.response.UnitOfMeasureResponse;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.service.IUnitOfMeasureService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("unit-of-measures")
public class UnitOfMeasureController {
    private final IUnitOfMeasureService unitOfMeasureService;

    public UnitOfMeasureController(IUnitOfMeasureService unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @PostMapping
    public ResponseEntity<?> createUnitOfMeasure(@Valid @RequestBody UnitOfMeasureRequest request) {
        log.info("POST /unit-of-measures called");
        UnitOfMeasure unitOfMeasure = unitOfMeasureService.createUnitOfMeasure(request);
        log.info("Created unit id={}", unitOfMeasure.getId());
        return ResponseEntity.ok(ApiResponse.builder()
                .data(unitOfMeasure)
                .message("Created unit of measure successfully")
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getPagedUnits(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size
    ) {
        log.info("GET /unit-of-measures called - keyword={}, page={}, size={}", keyword, page, size);
        PageCacheResponse<UnitOfMeasure> result = unitOfMeasureService.getUnitsByKeyword(keyword, page, size);

        List<UnitOfMeasureResponse> unitOfMeasureResponses = new ArrayList<>();
        for (UnitOfMeasure unitOfMeasure : result.getContent()) {
            unitOfMeasureResponses.add(UnitOfMeasureResponse.builder()
                    .id(unitOfMeasure.getId())
                    .code(unitOfMeasure.getCode())
                    .baseUnitCode(unitOfMeasure.getBaseUnitCode())
                    .factorToBase(unitOfMeasure.getFactorToBase())
                    .createdAt(unitOfMeasure.getCreatedAt())
                    .createdBy(unitOfMeasure.getCreatedBy())
                    .modifiedAt(unitOfMeasure.getModifiedAt())
                    .modifiedBy(unitOfMeasure.getModifiedBy())
                    .build());
        }
        log.info("Returning {} items (totalElements={})", unitOfMeasureResponses.size(), result.getTotalElements());
        return ResponseEntity.ok(ApiResponse.builder()
                .data(unitOfMeasureResponses)
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllUnits() {
        log.info("GET /unit-of-measures/all called");
        List<UnitOfMeasure> list = unitOfMeasureService.getAllUnits();
        log.info("Returned all units: count={}", list.size());
        return ResponseEntity.ok(ApiResponse.builder().data(list).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUnit(
            @PathVariable Long id,
            @RequestBody UnitOfMeasureRequest request) {
        log.info("PUT /unit-of-measures/{} called", id);
        UnitOfMeasure updated = unitOfMeasureService.editUnitOfMeasure(id, request);
        log.info("Updated unit id={}", id);
        return ResponseEntity.ok(ApiResponse.builder()
                .data(updated)
                .message("Updated unit of measure successfully")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUnit(@PathVariable Long id) {
        log.info("DELETE /unit-of-measures/{} called", id);
        unitOfMeasureService.deleteUnitOfMeasure(id);
        log.info("Deleted unit id={}", id);
        return ResponseEntity.ok(ApiResponse.builder()
                .message("Deleted unit with id = " + id)
                .build());

    }
}
