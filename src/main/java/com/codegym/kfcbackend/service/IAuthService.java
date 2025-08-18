package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.LoginRequest;
import com.codegym.kfcbackend.entity.User;

public interface IAuthService {
    User login(LoginRequest request);
}
