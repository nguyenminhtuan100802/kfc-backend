package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.entity.ComboItem;

import java.util.List;

public interface IComboItemService {
    List<ComboItem> getAllComboItemsByComboId(Long comboId);
}
