package com.codegym.kfcbackend.constant;

public class AppConstants {
    public static String PASSWORD_CHANGED_SUCCESS = "Password changed successfully, please login again";
    public static String USER_CREATED_SUCCESS = "User created successfully";
    public static String ROLE_NOT_FOUND = "Role not found";
    public static String CATEGORY_NOT_FOUND = "Category \"%s\" not found";
    public static final String ROLE_ALREADY_EXISTS = "Role with name \"%s\" already exists";
    public static final String PRODUCT_ALREADY_EXISTS = "Product with name \"%s\" already exists";
    public static final String CATEGORY_ALREADY_EXISTS = "Category with name \"%s\" already exists";
    public static final String LOGIN_FAILED = "Username or password is incorrect";
    public static final String USER_ALREADY_EXIST = "User already exists";
    public static final String PASSWORD_LENGTH_ERROR = "Password length must be at least %d characters";
    public static final String NEW_PASSWORD_IS_OLD = "New password is the same as old password";
    public static final String NEW_PASSWORD_IS_NOT_VALID = "New password need at least 1 uppercase, 1 lowercase, 1 digit and 1 special character";
    public static final String MESSAGE_TOKEN_ERROR = "Cannot create jwt token, error: %s";
    public static final String PRODUCT_PRICE_ERROR = "Price must be > 0";
    public static final String INFOMATION_EMPTY = "Some information are empty";
    public static final int MINIMUM_PASSWORD_CHARACTER = 4;
    public static final int PASSWORD_LENGTH = 10;


}
