package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.RolePermissionRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.PermissionResponse;
import com.codegym.kfcbackend.dto.response.RolePermissionResponse;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.entity.RolePermission;
import com.codegym.kfcbackend.service.IRolePermissionService;
import com.codegym.kfcbackend.service.IRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("role-permissions")
public class RolePermissionController {
    private final IRolePermissionService rolePermissionService;
    private final IRoleService roleService;

    public RolePermissionController(IRolePermissionService rolePermissionService,
                                    IRoleService roleService) {
        this.rolePermissionService = rolePermissionService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRolePermissions() {
        List<Role> roles = roleService.getAllRoles();
        List<RolePermissionResponse> rolePermissionResponses = new ArrayList<>();

        for (Role role : roles) {
            RolePermissionResponse rolePermissionResponse = RolePermissionResponse.builder()
                    .roleName(role.getName())
                    .permissions(new ArrayList<>())
                    .build();
            for (RolePermission rolePermission : role.getRolePermissions()) {
                PermissionResponse permissionResponse = PermissionResponse.builder()
                        .permissionName(rolePermission.getPermission().getName())
                        .isAllowed(rolePermission.isAllowed())
                        .build();
                rolePermissionResponse.getPermissions().add(permissionResponse);
            }
            rolePermissionResponses.add(rolePermissionResponse);
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .data(rolePermissionResponses)
                .build());
    }

    @PutMapping
    public ResponseEntity<?> updateRolePermission(@RequestBody RolePermissionRequest request) {
        try {
            RolePermission rolePermission = rolePermissionService.updateRolePermission(request);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Update role permission successfully")
                    .build());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .message(e.getMessage())
                    .build());
        }
    }
}
