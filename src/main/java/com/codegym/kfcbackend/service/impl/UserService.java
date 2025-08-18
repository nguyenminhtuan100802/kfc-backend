package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.ChangeDefaultPasswordRequest;
import com.codegym.kfcbackend.dto.request.LoginRequest;
import com.codegym.kfcbackend.dto.request.EmployeeRequest;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.repository.RoleRepository;
import com.codegym.kfcbackend.repository.UserRepository;
import com.codegym.kfcbackend.service.IUserService;
import com.codegym.kfcbackend.utils.PasswordUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User createUser(EmployeeRequest request) {
        Role existingRole = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new RuntimeException(AppConstants.ROLE_NOT_FOUND));

        User existingUser = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (existingUser != null) {
            throw new RuntimeException(AppConstants.USER_ALREADY_EXIST);
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .username(request.getUsername())
                .password(PasswordUtils.generateRandomPassword(AppConstants.PASSWORD_LENGTH))
                .isChangeDefaultPassword(false)
//                .userRoles(existingRole)
                .build();

        User savedUser = this.userRepository.save(user);
        return savedUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }
}
