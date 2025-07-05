package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.dto.request.RoleRquest;
import com.codegym.kfcbackend.dto.response.RoleResponse;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.service.impl.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {
    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody RoleRquest rquest) {
        try {
            Role result = roleService.createRole(rquest);
            return ResponseEntity.ok(result);
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleResponse> responses = new ArrayList<>();
        for (Role role : roles) {
            responses.add(RoleResponse.builder()
                    .name(role.getName())
                    .build());
        }
        return ResponseEntity.ok(responses);
    }

}
