package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.ChangeDefaultPasswordRequest;
import com.codegym.kfcbackend.dto.request.LoginRequest;
import com.codegym.kfcbackend.dto.request.EmployeeRequest;
import com.codegym.kfcbackend.entity.User;

import java.util.List;

public interface IUserService {
    User createUser(EmployeeRequest request);
    User login(LoginRequest request);
    List<User> getAllUsers();
    User changeDefaultPassword(ChangeDefaultPasswordRequest request);
}
