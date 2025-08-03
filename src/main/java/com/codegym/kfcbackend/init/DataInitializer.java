package com.codegym.kfcbackend.init;

import com.codegym.kfcbackend.constant.PermissionConstant;
import com.codegym.kfcbackend.entity.Permission;
import com.codegym.kfcbackend.entity.Role;
import com.codegym.kfcbackend.entity.RolePermission;
import com.codegym.kfcbackend.entity.User;
import com.codegym.kfcbackend.entity.UserRole;
import com.codegym.kfcbackend.repository.PermissionRepository;
import com.codegym.kfcbackend.repository.RolePermissionRepository;
import com.codegym.kfcbackend.repository.RoleRepository;
import com.codegym.kfcbackend.repository.UserRepository;
import com.codegym.kfcbackend.repository.UserRoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public DataInitializer(PermissionRepository permissionRepository,
                           RoleRepository roleRepository,
                           RolePermissionRepository rolePermissionRepository,
                           UserRepository userRepository,
                           UserRoleRepository userRoleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<String> allPermissions = List.of(
                // Product
                PermissionConstant.PRODUCT_VIEW,
                PermissionConstant.PRODUCT_CREATE,
                PermissionConstant.PRODUCT_UPDATE,
                PermissionConstant.PRODUCT_DELETE,
                // Combo
                PermissionConstant.COMBO_VIEW,
                PermissionConstant.COMBO_CREATE,
                PermissionConstant.COMBO_UPDATE,
                PermissionConstant.COMBO_DELETE,
                // Bill
                PermissionConstant.BILL_VIEW,
                PermissionConstant.BILL_CREATE,
                PermissionConstant.BILL_UPDATE,
                PermissionConstant.BILL_DELETE,
                // Ingredient Category
                PermissionConstant.INGREDIENT_CATEGORY_VIEW,
                PermissionConstant.INGREDIENT_CATEGORY_CREATE,
                PermissionConstant.INGREDIENT_CATEGORY_UPDATE,
                PermissionConstant.INGREDIENT_CATEGORY_DELETE,
                // Combo Category
                PermissionConstant.COMBO_CATEGORY_VIEW,
                PermissionConstant.COMBO_CATEGORY_CREATE,
                PermissionConstant.COMBO_CATEGORY_UPDATE,
                PermissionConstant.COMBO_CATEGORY_DELETE,
                // Product Category
                PermissionConstant.PRODUCT_CATEGORY_VIEW,
                PermissionConstant.PRODUCT_CATEGORY_CREATE,
                PermissionConstant.PRODUCT_CATEGORY_UPDATE,
                PermissionConstant.PRODUCT_CATEGORY_DELETE,
                // Unit of Measure
                PermissionConstant.UNIT_OF_MEASURE_VIEW,
                PermissionConstant.UNIT_OF_MEASURE_CREATE,
                PermissionConstant.UNIT_OF_MEASURE_UPDATE,
                PermissionConstant.UNIT_OF_MEASURE_DELETE,
                // Ingredient
                PermissionConstant.INGREDIENT_VIEW,
                PermissionConstant.INGREDIENT_CREATE,
                PermissionConstant.INGREDIENT_UPDATE,
                PermissionConstant.INGREDIENT_DELETE,
                // Stock Entry
                PermissionConstant.STOCK_ENTRY_VIEW,
                PermissionConstant.STOCK_ENTRY_CREATE,
                PermissionConstant.STOCK_ENTRY_UPDATE,
                PermissionConstant.STOCK_ENTRY_DELETE,
                // Role
                PermissionConstant.ROLE_VIEW,
                PermissionConstant.ROLE_CREATE,
                PermissionConstant.ROLE_UPDATE,
                PermissionConstant.ROLE_DELETE,
//                Role Permission
                PermissionConstant.ROLE_PERMISSION_VIEW,
                PermissionConstant.ROLE_PERMISSION_UPDATE,
//                Permission
                PermissionConstant.PERMISSION_VIEW,
                PermissionConstant.PERMISSION_CREATE,
                PermissionConstant.PERMISSION_UPDATE,
                PermissionConstant.PERMISSION_DELETE,
                // User
                PermissionConstant.EMPLOYEE_CREATE
        );

        for (String permission : allPermissions) {
            if (permissionRepository.findByName(permission).isEmpty()) {
                permissionRepository.save(Permission.builder()
                        .name(permission)
                        .build());
                System.out.println(">> Added permission: " + permission);
            }
        }

        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> {
                    Role role = Role.builder()
                            .name("ADMIN")
                            .build();
                    return roleRepository.save(role);
                });

        List<Permission> permissions = permissionRepository.findAll();
        for (Permission permission : permissions) {
            if (rolePermissionRepository.findByRoleIdAndPermissionId(adminRole.getId(), permission.getId()).isEmpty()) {
                RolePermission rolePermission = RolePermission.builder()
                        .role(adminRole)
                        .permission(permission)
                        .isAllowed(true)
                        .build();
                rolePermissionRepository.save(rolePermission);
                System.out.println(">> Added permission: " + permission.getName() + " to role: " + adminRole.getName());
            }
        }

        User existingAdminUser = userRepository.findByUsername("nguyenminhtuan").orElse(null);
        if (existingAdminUser == null) {
            User adminUser = User.builder()
                    .username("nguyenminhtuan")
                    .password("123456")
                    .isChangeDefaultPassword(true)
                    .build();
            User savedAdminUser = userRepository.save(adminUser);
            System.out.println(">> Added user: " + savedAdminUser.getUsername());
            System.out.println(">> Default password: " + savedAdminUser.getPassword());

            UserRole userRole = UserRole.builder()
                    .user(savedAdminUser)
                    .role(adminRole)
                    .build();

            UserRole savedUserRole = userRoleRepository.save(userRole);
            System.out.println(">> Added user: " + savedUserRole.getUser().getUsername() + " to role: " + savedUserRole.getRole().getName());
        }
        else {
            System.out.println(">> Existing user: " + existingAdminUser.getUsername());
            UserRole existingUserRole = userRoleRepository.findByUserIdAndRoleId(existingAdminUser.getId(), adminRole.getId()).orElse(null);
            if (existingUserRole == null) {
                UserRole userRole = UserRole.builder()
                        .user(existingAdminUser)
                        .role(adminRole)
                        .build();
                UserRole savedUserRole = userRoleRepository.save(userRole);
                System.out.println(">> Added user: " + savedUserRole.getUser().getUsername() + " to role: " + savedUserRole.getRole().getName());
            }
        }
    }
}
