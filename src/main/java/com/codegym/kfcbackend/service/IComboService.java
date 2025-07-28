package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.ComboRequest;
import com.codegym.kfcbackend.entity.Combo;

import java.util.List;

public interface IComboService {
    Combo createCombo(ComboRequest request);
    List<Combo> getAllCombos();
    Combo editCombo(Long id, ComboRequest request);
    void deleteCombo(Long id);
}
