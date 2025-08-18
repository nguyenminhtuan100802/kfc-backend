package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.entity.ComboItem;
import com.codegym.kfcbackend.repository.ComboItemRepository;
import com.codegym.kfcbackend.service.IComboItemService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComboItemService implements IComboItemService {
    private final ComboItemRepository comboItemRepository;

    public ComboItemService(ComboItemRepository comboItemRepository) {
        this.comboItemRepository = comboItemRepository;
    }

    @Override
    public List<ComboItem> getAllComboItemsByComboId(Long comboId) {
        List<ComboItem> comboItems = comboItemRepository.findAllByComboId(comboId);
        return comboItems;
    }
}
