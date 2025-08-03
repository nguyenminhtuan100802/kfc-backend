package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.RolePermissionRequest;
import com.codegym.kfcbackend.entity.RolePermission;

import java.util.List;

public interface IRolePermissionService {
    RolePermission updateRolePermission(RolePermissionRequest request);
}
