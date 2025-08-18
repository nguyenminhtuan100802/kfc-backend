package com.codegym.kfcbackend.controller;

import com.codegym.kfcbackend.entity.Test;
import com.codegym.kfcbackend.repository.TestRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    private final TestRepository testRepository;

    public TestController(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @PostMapping
    public ResponseEntity<?> createTest() {
        try {
            long count = 1000;
            for (int i = 0; i < count; i++) {
                testRepository.save(Test.builder()
                        .name("Test " + i)
                        .build());
            }
            System.out.println(">> Saved " + count + " tests");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getTest() {
        try {
            List<Test> tests = testRepository.findAll();
            return ResponseEntity.ok(tests);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

}
