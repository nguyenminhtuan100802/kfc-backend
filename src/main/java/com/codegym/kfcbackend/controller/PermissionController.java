package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.PermissionRequest;
import com.codegym.kfcbackend.dto.response.ApiResponse;
import com.codegym.kfcbackend.dto.response.PermissionResponse;
import com.codegym.kfcbackend.entity.Permission;
import com.codegym.kfcbackend.service.IPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("permissions")
public class PermissionController {
    private final IPermissionService permissionService;

    public PermissionController(IPermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody PermissionRequest rquest) {
        try {
            Permission permission = permissionService.createPermission(rquest);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Create permission successfully")
                    .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        List<PermissionResponse> responses = new ArrayList<>();
        for (Permission permission : permissions) {
            responses.add(PermissionResponse.builder()
                    .id(permission.getId())
                    .permissionName(permission.getName())
                    .build());
        }
        return ResponseEntity.ok(ApiResponse.builder()
                .data(responses)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editPermission(
            @PathVariable Long id,
            @RequestBody PermissionRequest request
    ) {
        try {
            Permission updated = permissionService.editPermission(id, request);
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .message("Updated permission successfully")
                            .build()
            );
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePermission(@PathVariable Long id) {
        try {
            permissionService.deletePermission(id);
            return ResponseEntity.ok(ApiResponse.builder()
                    .message("Deleted permission successfully")
                    .build());
        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }
}
