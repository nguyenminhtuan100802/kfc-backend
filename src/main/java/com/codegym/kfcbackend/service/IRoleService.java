package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.RoleRequest;
import com.codegym.kfcbackend.entity.Role;

import java.util.List;

public interface IRoleService {
    Role createRole(RoleRequest request);
    List<Role> getAllRoles();
    Role editRole(Long id, RoleRequest request);
    void deleteRole(Long id);
}
