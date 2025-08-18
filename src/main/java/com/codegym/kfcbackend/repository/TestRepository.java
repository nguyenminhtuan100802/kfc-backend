package com.codegym.kfcbackend.repository;

import com.codegym.kfcbackend.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestRepository extends JpaRepository<Test, Long> {
}
