package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ChangeDefaultPasswordRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.LoginResponse;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.service.IPasswordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("passwords")
public class PasswordController {
    private final IPasswordService passwordService;

    public PasswordController(IPasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping(value = "change-default-password")
    public ResponseEntity<?> changeDefaultPassword(@RequestBody ChangeDefaultPasswordRequest request) {
        try {
            User existingUser = passwordService.changeDefaultPassword(request);
            LoginResponse response = LoginResponse.builder()
                    .username(existingUser.getUsername())
                    .roleName(existingUser.getUserRoles().get(0).getRole().getName())
                    .isChangeDefaultPassword(existingUser.isChangeDefaultPassword())
                    .build();
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(response)
                    .message(AppConstants.PASSWORD_CHANGED_SUCCESS)
                    .build());
//            return ResponseEntity.badRequest().body(result);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
