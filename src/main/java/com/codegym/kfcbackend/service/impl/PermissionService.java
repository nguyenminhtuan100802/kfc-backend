package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.PermissionRequest;
import com.codegym.kfcbackend.entity.Permission;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.entity.RolePermission;
import com.codegym.kfcbackend.repository.PermissionRepository;
import com.codegym.kfcbackend.repository.RolePermissionRepository;
import com.codegym.kfcbackend.repository.RoleRepository;
import com.codegym.kfcbackend.service.IPermissionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService implements IPermissionService {
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;

    public PermissionService(PermissionRepository permissionRepository,
                             RolePermissionRepository rolePermissionRepository,
                             RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Permission createPermission(PermissionRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String permissionName = request.getName().trim().toUpperCase();
        Permission existingPermission = permissionRepository.findByName(permissionName).orElse(null);
        if (existingPermission != null) {
            throw new RuntimeException("Permission already exists: " + permissionName);
        }
        Permission permission = Permission.builder()
                .name(permissionName)
                .build();
        Permission savedPermission = permissionRepository.save(permission);

        List<Role> roles = roleRepository.findAll();
        for (Role role : roles) {
            RolePermission existingRolePermission = rolePermissionRepository.findByRoleNameAndPermissionName(role.getName(), permissionName).orElse(null);
            if (existingRolePermission == null) {
                RolePermission rolePermission = RolePermission.builder()
                        .role(role)
                        .permission(savedPermission)
                        .isAllowed(false)
                        .build();
                rolePermissionRepository.save(rolePermission);
            }
        }
        return savedPermission;
    }

    @Override
    public List<Permission> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAllAndOrderByName();
        return permissions;
    }

    @Override
    public Permission editPermission(Long id, PermissionRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String permissionName = request.getName().trim().toUpperCase();
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id=" + id));
        if (existingPermission != null && !existingPermission.getId().equals(id)) {
            throw new RuntimeException("Permission name already exists: " + permissionName);
        }
        existingPermission.setName(permissionName);
        Permission savedPermission = permissionRepository.save(existingPermission);
        return savedPermission;
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        Permission existingPermission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id=" + id));
        rolePermissionRepository.deleteByPermissionId(existingPermission.getId());
        permissionRepository.deleteById(existingPermission.getId());
    }
}
