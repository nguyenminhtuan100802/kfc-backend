package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.ComboCategoryResponse;
import com.codegym.kfcbackend.dto.response.ComboResponse;
import com.codegym.kfcbackend.entity.Combo;
import com.codegym.kfcbackend.entity.ComboCategory;
import com.codegym.kfcbackend.service.IComboCategoryService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("combo-categories")
public class ComboCategoryController {
    private final IComboCategoryService comboCategoryService;

    public ComboCategoryController(IComboCategoryService comboCategoryService) {
        this.comboCategoryService = comboCategoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            List<ComboCategory> comboCategories = comboCategoryService.getAll();
            List<ComboCategoryResponse> comboCategoryResponses = new ArrayList<>();
            for (ComboCategory comboCategory : comboCategories) {
                ComboCategoryResponse comboCategoryResponse = ComboCategoryResponse.builder()
                        .id(comboCategory.getId())
                        .name(comboCategory.getName())
                        .combos(new ArrayList<>())
                        .build();
                for (Combo combo : comboCategory.getCombos()) {
                    ComboResponse comboResponse = ComboResponse.builder()
                            .name(combo.getName())
                            .build();
                    comboCategoryResponse.getCombos().add(comboResponse);
                }
                comboCategoryResponses.add(comboCategoryResponse);
            }
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Lấy danh sách nhóm combo thành công")
                            .data(comboCategoryResponses)
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    ApiResponse.builder()
                            .message("Lỗi khi lấy danh sách nhóm combo: " + e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CategoryRequest request) {
        try {
            ComboCategory created = comboCategoryService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    ApiResponse.builder()
                            .message("Tạo nhóm combo thành công")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody CategoryRequest request) {
        try {
            ComboCategory updated = comboCategoryService.update(id, request);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Cập nhật nhóm combo thành công")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            comboCategoryService.delete(id);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Xóa nhóm combo thành công")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
