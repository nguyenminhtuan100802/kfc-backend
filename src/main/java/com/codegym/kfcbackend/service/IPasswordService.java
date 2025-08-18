package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.ChangeDefaultPasswordRequest;
import com.codegym.kfcbackend.entity.User;

public interface IPasswordService {
    User changeDefaultPassword(ChangeDefaultPasswordRequest request);
}
