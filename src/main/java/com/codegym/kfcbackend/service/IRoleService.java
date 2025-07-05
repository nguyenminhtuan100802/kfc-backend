package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.RoleRquest;
import com.codegym.kfcbackend.entity.Role;

import java.util.List;

public interface IRoleService {
    Role createRole(RoleRquest request);
    List<Role> getAllRoles();
}
