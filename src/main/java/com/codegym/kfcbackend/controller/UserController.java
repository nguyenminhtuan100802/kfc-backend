package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ChangeDefaultPasswordRequest;
import com.codegym.kfcbackend.dto.request.LoginRequest;
import com.codegym.kfcbackend.dto.request.EmployeeRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.LoginResponse;
import com.codegym.kfcbackend.dto.response.UserResponse;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.service.IUserService;
import com.codegym.kfcbackend.service.impl.JwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "create-employee")
    public ResponseEntity<?> createEmployee(@RequestBody EmployeeRequest request) {
        try {
            User result = userService.createUser(request);
            UserResponse response = UserResponse.builder()
                    .fullName(result.getFullName())
                    .username(result.getUsername())
                    .password(result.getPassword())
                    .roleName(result.getUserRoles().get(0).getRole().getName())
                    .build();

            return ResponseEntity.ok(ApiResponse.builder()
                    .data(response)
                    .message(AppConstants.USER_CREATED_SUCCESS)
                    .build());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUser() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = new ArrayList<>();
        for (User user : users) {
            responses.add(UserResponse.builder()
                    .fullName(user.getFullName())
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roleName(user.getUserRoles().get(0).getRole().getName())
                    .build());
        }
        return ResponseEntity.ok(responses);
    }
}
