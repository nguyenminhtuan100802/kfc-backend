package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.RolePermission;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.permission.id = :permissionId")
    Optional<RolePermission> findByRoleIdAndPermissionId(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);

    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.name = :roleName AND rp.permission.name = :permissionName")
    Optional<RolePermission> findByRoleNameAndPermissionName(@Param("roleName") String roleName, @Param("permissionName") String permissionName);


    @Query("""
                SELECT rp
                FROM RolePermission rp
                ORDER BY rp.role.name ASC, rp.permission.name ASC
            """)
    List<RolePermission> findAllOrderByRoleAndPermission();

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Long permissionId);
}
