package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.dto.request.PermissionRequest;
import com.codegym.kfcbackend.entity.Permission;

import java.util.List;

public interface IPermissionService {
    Permission createPermission(PermissionRequest request);
    List<Permission> getAllPermissions();
    Permission editPermission(Long id, PermissionRequest request);
    void deletePermission(Long id);
}
