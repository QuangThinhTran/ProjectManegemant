package com.vn.projectmanagement.common.swagger;

public class SwaggerMessages {
    // HTTP Status Messages
    public static final String OK = "Request successful.";
    public static final String BAD_REQUEST = "Bad request. Please check the request parameters.";
    public static final String FORBIDDEN = "Access forbidden. You do not have permission to access this resource.";
    public static final String UNAUTHORIZED = "Unauthorized. Please provide valid credentials.";
    public static final String INTERNAL_SERVER_ERROR = "Internal server error. Please try again later.";
    public static final String INVALID_JWT_TOKEN = "JWT token is expired or invalid";

    public static final String BAD_REQUEST_MESSAGE = "{\"message\": \"" + SwaggerMessages.BAD_REQUEST + "\"}";
    public static final String FORBIDDEN_MESSAGE = "{\"message\": \"" + SwaggerMessages.FORBIDDEN + "\"}";
    public static final String UNAUTHORIZED_MESSAGE = "{\"message\": \"" + SwaggerMessages.UNAUTHORIZED + "\"}";
    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "{\"message\": \"" + SwaggerMessages.INTERNAL_SERVER_ERROR + "\"}";

    // Registration Messages
    public static final String REGISTRATION_SUCCESS_MESSAGE = "Account registration successful.";
    public static final String REGISTRATION_FAILURE_MESSAGE = "Failed to register account.";

    // Login Messages
    public static final String LOGIN_SUCCESS_MESSAGE = "Login successful.";
    public static final String LOGIN_FAILURE_MESSAGE = "Failed to login.";

    // Login Messages
    public static final String LOGOUT_SUCCESS_MESSAGE = "Logout successful.";

    // Role Messages
    public static final String GET_ALL_ROLES = "Get all roles";
    public static final String CREATE_ROLE = "Create role successfully";
    public static final String UPDATE_ROLE = "Update role successfully";
    public static final String DELETE_ROLE = "Delete role successfully";
    public static final String CREATE_ROLE_MESSAGE =  "{\"message\": \"" + SwaggerMessages.CREATE_ROLE + "\"}";
    public static final String UPDATE_ROLE_MESSAGE =  "{\"message\": \"" + SwaggerMessages.UPDATE_ROLE + "\"}";
    public static final String DELETE_ROLE_MESSAGE =  "{\"message\": \"" + SwaggerMessages.DELETE_ROLE + "\"}";

    // User Messages
    public static final String GET_ALL_USERS = "Get all users";
    public static final String GET_USER_BY_USERNAME = "Get user by username";
    public static final String CHANGE_PASSWORD = "Change password successfully";
    public static final String UPDATE_USER = "Update profile user successfully";
    public static final String DELETE_USER = "Delete user successfully";
    public static final String GET_USER_BY_USERNAME_MESSAGE =  "{\"message\": \"" + SwaggerMessages.GET_USER_BY_USERNAME + "\"}";
    public static final String CHANGE_PASSWORD_MESSAGE =  "{\"message\": \"" + SwaggerMessages.CHANGE_PASSWORD + "\"}";
    public static final String UPDATE_USER_MESSAGE =  "{\"message\": \"" + SwaggerMessages.UPDATE_USER + "\"}";
    public static final String DELETE_USER_MESSAGE =  "{\"message\": \"" + SwaggerMessages.DELETE_USER + "\"}";

    // File Messages
    public static final String UPLOAD_FILE = "Upload file successfully";
    public static final String UPLOAD_FILE_MESSAGE =  "{\"message\": \"" + SwaggerMessages.UPLOAD_FILE + "\"}";
}
