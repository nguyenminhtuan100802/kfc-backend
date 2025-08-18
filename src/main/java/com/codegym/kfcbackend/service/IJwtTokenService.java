package com.codegym.kfcbackend.service;

import com.codegym.kfcbackend.entity.User;

public interface IJwtTokenService {
    String generateToken(User user) throws Exception;
    boolean isTokenExpired(String token);
    String extractUsername(String token);
    boolean validateToken(String token, User userDetails);
}
