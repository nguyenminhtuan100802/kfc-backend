package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ChangeDefaultPasswordRequest;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.repository.UserRepository;
import com.codegym.kfcbackend.service.IPasswordService;
import com.codegym.kfcbackend.utils.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class PasswordService implements IPasswordService {
    private final UserRepository userRepository;

    public PasswordService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User changeDefaultPassword(ChangeDefaultPasswordRequest request) {
        User existingUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException(AppConstants.LOGIN_FAILED));

        if (!request.getCurrentPassword().equals(existingUser.getPassword())) {
            throw new RuntimeException(AppConstants.LOGIN_FAILED);
        }

        if (request.getNewPassword().equals(existingUser.getPassword())) {
            throw new RuntimeException(AppConstants.NEW_PASSWORD_IS_OLD);
        }

        if (!PasswordUtils.isValidPassword(request.getNewPassword())) {
            throw new RuntimeException(AppConstants.NEW_PASSWORD_IS_NOT_VALID);
        }

        existingUser.setPassword(request.getNewPassword());
        existingUser.setChangeDefaultPassword(true);
        User savedUser = userRepository.save(existingUser);
        return savedUser;
    }
}
