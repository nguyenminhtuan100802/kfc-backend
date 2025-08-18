package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.LoginRequest;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.repository.UserRepository;
import com.codegym.kfcbackend.service.IAuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements IAuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(LoginRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException(AppConstants.LOGIN_FAILED));

        if (!request.getPassword().equals(existingUser.getPassword())) {
            throw new RuntimeException(AppConstants.LOGIN_FAILED);
        }

        return existingUser;
    }
}
