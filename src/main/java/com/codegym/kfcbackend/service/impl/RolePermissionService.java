package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.dto.request.RolePermissionRequest;
import com.codegym.kfcbackend.entity.RolePermission;
import com.codegym.kfcbackend.repository.RolePermissionRepository;
import com.codegym.kfcbackend.service.IRolePermissionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionService implements IRolePermissionService {
    private final RolePermissionRepository rolePermissionRepository;

    public RolePermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional
    public RolePermission updateRolePermission(RolePermissionRequest request) {
        RolePermission existingRolePermission = rolePermissionRepository.findByRoleNameAndPermissionName(request.getRoleName(), request.getPermissionName())
                .orElseThrow(() -> new RuntimeException("Role permission not found: " + request.getRoleName() + " " + request.getPermissionName()));
        existingRolePermission.setAllowed(!existingRolePermission.isAllowed());
        RolePermission savedRolePermission = rolePermissionRepository.save(existingRolePermission);
        return savedRolePermission;
    }
}
