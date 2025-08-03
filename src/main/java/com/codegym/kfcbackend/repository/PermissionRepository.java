package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    @Query("SELECT p FROM Permission p WHERE p.name = :name")
    Optional<Permission> findByName(String name);

    @Query("SELECT p FROM Permission p ORDER BY p.name ASC")
    List<Permission> findAllAndOrderByName();
}
