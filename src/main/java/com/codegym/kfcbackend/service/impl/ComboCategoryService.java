package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.dto.request.CategoryRequest;
import com.codegym.kfcbackend.entity.ComboCategory;
import com.codegym.kfcbackend.repository.ComboCategoryRepository;
import com.codegym.kfcbackend.service.IComboCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComboCategoryService implements IComboCategoryService {
    private final ComboCategoryRepository comboCategoryRepository;

    public ComboCategoryService(ComboCategoryRepository comboCategoryRepository) {
        this.comboCategoryRepository = comboCategoryRepository;
    }

    @Override
    public List<ComboCategory> getAll() {
        return comboCategoryRepository.findAll();
    }

    @Override
    public ComboCategory create(CategoryRequest req) {
        String name = req.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        ComboCategory existing = comboCategoryRepository.findByName(name.trim()).orElse(null);
        if (existing != null) {
            throw new RuntimeException(String.format("Nhóm combo '%s' đã tồn tại", name.trim()));
        }
        ComboCategory category = ComboCategory.builder()
                .name(name.trim())
                .build();
        return comboCategoryRepository.save(category);
    }

    @Override
    public ComboCategory update(Long id, CategoryRequest req) {
        String name = req.getName();
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tên nhóm không được để trống");
        }
        name = name.trim();
        ComboCategory conflict = comboCategoryRepository.findByName(name).orElse(null);
        if (conflict != null && !conflict.getId().equals(id)) {
            throw new RuntimeException(String.format("Nhóm combo '%s' đã tồn tại", name));
        }
        ComboCategory existing = comboCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhóm combo không tìm thấy với id=" + id));
        existing.setName(name);
        return comboCategoryRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        ComboCategory existing = comboCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nhóm combo không tìm thấy với id=" + id));
        if (!existing.getCombos().isEmpty()) {
            throw new RuntimeException("Không thể xóa do vẫn còn các combo bên trong danh mục: " + existing.getName());
        }
        comboCategoryRepository.delete(existing);
    }
}
