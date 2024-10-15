package com.enigma.tokonyadia_api.constant;

public class Constant {

    public static final String USER_API = "/api/users";
    public static final String CUSTOMER_API = "/api/customers";
    public static final String ADMIN_STORE_API = "/api/admin-store";
    public static final String AUTH_API = "/api/auth";

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";


    public static final String ERROR_USERNAME_DUPLICATE = "username already exist";
    public static final String ERROR_ROLE_NOT_FOUND = "role not found";

    public static final String SUCCESS_CREATE_USER = "Successfully created new user";
    public static final String SUCCESS_GET_USER_INFO = "Success fetch user info";
    public static final String SUCCESS_UPDATE_PASSWORD = "Success update password.";

    public static final String ERROR_CUSTOMER_NOT_FOUND = "customer not found";
    public static final String SUCCESS_GET_CUSTOMER_BY_ID = "Successfully fetch menu by id";
    public static final String SUCCESS_GET_ALL_CUSTOMER = "Successfully fetch all customer";
    public static final String SUCCESS_UPDATE_CUSTOMER = "Successfully updated menu";
    public static final String SUCCESS_DELETE_CUSTOMER = "Successfully deleted menu";
    public static final String SUCCESS_CREATE_CUSTOMER = "Successfully created new customer";

    public static final String ERROR_ADMIN_STORE_NOT_FOUND = "admin store not found";
    public static final String SUCCESS_CREATE_ADMIN_STORE = "Successfully created new admin store";
    public static final String SUCCESS_GET_ADMIN_STORE_BY_ID = "Successfully fetch customer by id" ;
    public static final String SUCCESS_GET_ALL_ADMIN_STORE = "Successfully fetch all admin store";
    public static final String SUCCESS_UPDATE_ADMIN_STORE = "Successfully updated admin store";
    public static final String SUCCESS_DELETE_ADMIN_STORE = "Successfully delete admin store";

    public static final String SUCCESS_LOGIN = "Login Successfully";
    public static final String OK = "OK";

    public static final String ERROR_CREATE_JWT = "Error creating JWT Token";
    public static final String REFRESH_TOKEN_REQUIRED = "Refresh Token is required";

}
