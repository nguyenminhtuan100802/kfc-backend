package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.RoleRequest;
import com.codegym.kfcbackend.entity.Permission;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.entity.RolePermission;
import com.codegym.kfcbackend.repository.PermissionRepository;
import com.codegym.kfcbackend.repository.RolePermissionRepository;
import com.codegym.kfcbackend.repository.RoleRepository;
import com.codegym.kfcbackend.service.IRoleService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleService(RoleRepository roleRepository,
                       PermissionRepository permissionRepository,
                       RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    @Override
    @Transactional
    public Role createRole(RoleRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String roleName = request.getName().trim().toUpperCase();
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isPresent()) {
            throw new RuntimeException(String.format(AppConstants.ROLE_ALREADY_EXISTS, roleName));
        }
        Role role = Role.builder()
                .name(roleName)
                .build();
        Role savedRole = roleRepository.save(role);

        List<Permission> permissions = permissionRepository.findAll();
        for (Permission permission : permissions) {
            RolePermission rolePermission = rolePermissionRepository.findByRoleNameAndPermissionName(savedRole.getName(), permission.getName()).orElse(null);
            if (rolePermission == null) {
                rolePermission = RolePermission.builder()
                        .role(savedRole)
                        .permission(permission)
                        .isAllowed(false)
                        .build();
                rolePermissionRepository.save(rolePermission);
            }
        }
        return savedRole;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAllByOrderByNameAsc();
        return roles;
    }

    @Override
    public Role editRole(Long id, RoleRequest request) {
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException(AppConstants.INFORMATION_EMPTY);
        }
        String roleName = request.getName().trim().toUpperCase();
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id=" + id));
        if (existingRole != null && !existingRole.getId().equals(id)) {
            throw new RuntimeException(String.format(AppConstants.ROLE_ALREADY_EXISTS, roleName));
        }
        existingRole.setName(roleName);
        Role savedRole = roleRepository.save(existingRole);
        return savedRole;
    }

    @Override
    public void deleteRole(Long id) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id=" + id));

        roleRepository.delete(existingRole);
    }
}
