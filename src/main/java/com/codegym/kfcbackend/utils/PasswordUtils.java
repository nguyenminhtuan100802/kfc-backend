package com.codegym.kfcbackend.utils;

import com.codegym.kfcbackend.constant.AppConstants;

import java.security.SecureRandom;

public class PasswordUtils {
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = UPPER.toLowerCase();
    private static final String DIGITS = "0123456789";
    private static final String SPECIALS = "!@#$%^&*()-_=+[]{}|;:,.<>?";
    private static final String ALL_CHARS = UPPER + LOWER + DIGITS + SPECIALS;
    private static final SecureRandom random = new SecureRandom();


    public static String generateRandomPassword(int length) {
        if (length < AppConstants.MINIMUM_PASSWORD_CHARACTER) {
            throw new RuntimeException(String.format(AppConstants.PASSWORD_LENGTH_ERROR,
                    AppConstants.MINIMUM_PASSWORD_CHARACTER));
        }

        StringBuilder pw = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            pw.append(ALL_CHARS.charAt(random.nextInt(ALL_CHARS.length())));
        }
        return pw.toString();
    }

    public static boolean isValidPassword(String password) {
        if (password.length() <= AppConstants.MINIMUM_PASSWORD_CHARACTER) {
            throw new RuntimeException(String.format(AppConstants.PASSWORD_LENGTH_ERROR,
                    AppConstants.MINIMUM_PASSWORD_CHARACTER));
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (UPPER.indexOf(c) >= 0) {
                hasUpper = true;
            } else if (LOWER.indexOf(c) >= 0) {
                hasLower = true;
            } else if (DIGITS.indexOf(c) >= 0) {
                hasDigit = true;
            } else if (SPECIALS.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }
}
