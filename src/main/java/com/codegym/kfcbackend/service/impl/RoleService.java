package com.codegym.kfcbackend.service.impl;

import com.codegym.kfcbackend.constant.AppConstants;
import com.codegym.kfcbackend.dto.request.RoleRquest;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.repository.RoleRepository;
import com.codegym.kfcbackend.service.IRoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService implements IRoleService {
    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role createRole(RoleRquest request) {
        String roleName = request.getName().trim();
        Optional<Role> existingRole = roleRepository.findByName(roleName);
        if (existingRole.isPresent()) {
            throw new RuntimeException(String.format(AppConstants.ROLE_ALREADY_EXISTS, roleName));
        }
        Role role = Role.builder()
                .name(request.getName())
                .build();
        Role savedRole = roleRepository.save(role);
        return savedRole;
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }
}
