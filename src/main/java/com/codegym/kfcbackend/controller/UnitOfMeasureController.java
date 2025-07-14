package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.UnitOfMeasureRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.entity.UnitOfMeasure;
import com.codegym.kfcbackend.service.IUnitOfMeasureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("unit-of-measures")
public class UnitOfMeasureController {
    private final IUnitOfMeasureService unitOfMeasureService;

    public UnitOfMeasureController(IUnitOfMeasureService unitOfMeasureService) {
        this.unitOfMeasureService = unitOfMeasureService;
    }

    @PostMapping
    public ResponseEntity<?> createUnitOfMeasure(@RequestBody UnitOfMeasureRequest request) {
        try {
            UnitOfMeasure unitOfMeasure = unitOfMeasureService.createUnitOfMeasure(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(unitOfMeasure)
                    .message("Created unit of measure successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllUnits() {
        List<UnitOfMeasure> list = unitOfMeasureService.getAllUnitOfMeasures();
        return ResponseEntity.ok(ApiResponse.builder()
                .data(list)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateUnit(
            @PathVariable Long id,
            @RequestBody UnitOfMeasureRequest request) {
        try {
            UnitOfMeasure updated = unitOfMeasureService.editUnitOfMeasure(id, request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(updated)
                    .message("Updated unit of measure successfully")
                    .build());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(ex.getMessage())
                    .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUnit(@PathVariable Long id) {
        try {
            unitOfMeasureService.deleteUnitOfMeasure(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Deleted unit with id = " + id)
                    .build());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.builder()
                    .message(ex.getMessage())
                    .build());
        }
    }
}
