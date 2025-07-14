package com.codegym.kfcbackend.constant;

public class AppConstants {
    public static String PASSWORD_CHANGED_SUCCESS = "Password changed successfully, please login again";
    public static String USER_CREATED_SUCCESS = "User created successfully";
    public static String PRODUCT_CREATED_SUCCESS = "Product created successfully";
    public static String COMBO_CREATED_SUCCESS = "Combo created successfully";
    public static String ROLE_NOT_FOUND = "Role not found";
    public static String CATEGORY_NOT_FOUND = "Category \"%s\" not found";
    public static String PRODUCT_NOT_FOUND = "Product \"%s\" not found";
    public static String UNIT_OF_MEASURE_NOT_FOUND = "Unit of measure not found with base unit code: %s!";
    public static String UNIT_OF_MEASURE_NOT_FOUND_WITH_ID = "Unit of measure not found with id: %d!";
    public static String INGREDIENT_NOT_FOUND_WITH_ID = "Ingredient not found with id: %d!";
    public static final String ROLE_ALREADY_EXISTS = "Role with name \"%s\" already exists";
    public static final String PRODUCT_ALREADY_EXISTS = "Product with name \"%s\" already exists";
    public static final String CATEGORY_ALREADY_EXISTS = "Category with name \"%s\" already exists";
    public static final String COMBO_ALREADY_EXISTS = "Combo with name \"%s\" already exists";
    public static final String UNIT_OF_MEASURE_ALREADY_EXISTS = "Unit of measure with code %s already exists";
    public static final String INGREDIENT_ALREADY_EXISTS = "Ingredient already exists: %s";
    public static final String LOGIN_FAILED = "Username or password is incorrect";
    public static final String USER_ALREADY_EXIST = "User already exists";
    public static final String PASSWORD_LENGTH_ERROR = "Password length must be at least %d characters";
    public static final String NEW_PASSWORD_IS_OLD = "New password is the same as old password";
    public static final String NEW_PASSWORD_IS_NOT_VALID = "New password need at least 1 uppercase, 1 lowercase, 1 digit and 1 special character";
    public static final String MESSAGE_TOKEN_ERROR = "Cannot create jwt token, error: %s";
    public static final String PRODUCT_PRICE_ERROR = "Price must be > 0";
    public static final String PRODUCT_QUANTITY_ERROR = "Product quantity must be > 0";
    public static final String FACTOR_TO_BASE_ERROR = "Factor to base must be > 0";
    public static final String INGREDIENT_QUANTITY_ERROR = "Ingredient quantity must be > 0";
    public static final String COMBO_DATE_ERROR = "Promotion start date (%s) must not be after end date (%s).";
    public static final String COMBO_TIME_ERROR = "Promotion start time (%s) must not be after end time (%s).";
    public static final String DISCOUNT_ERROR = "Discount amount is greater than total price!";
    public static final String UNIT_OF_MEASURE_ERROR = "Base unit code cannot be the same as the code of the unit of measure";
    public static final String INFORMATION_EMPTY = "Some information are empty";
    public static final int MINIMUM_PASSWORD_CHARACTER = 4;
    public static final int PASSWORD_LENGTH = 10;


}
