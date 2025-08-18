package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.LoginRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.LoginResponse;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.service.IAuthService;
import com.codegym.kfcbackend.service.IJwtTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auths")
public class AuthController {
    private final IAuthService authService;
    private final IJwtTokenService jwtTokenService;

    public AuthController(IAuthService authService,
                          IJwtTokenService jwtTokenService) {
        this.authService = authService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping(value = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            User existingUser = authService.login(request);
            String token = jwtTokenService.generateToken(existingUser);
            LoginResponse response = LoginResponse.builder()
                    .username(existingUser.getUsername())
                    .roleName(existingUser.getUserRoles().get(0).getRole().getName())
                    .isChangeDefaultPassword(existingUser.isChangeDefaultPassword())
                    .token(token)
                    .build();
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(response)
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
