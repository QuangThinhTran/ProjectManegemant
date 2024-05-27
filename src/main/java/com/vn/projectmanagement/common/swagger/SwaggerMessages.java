package com.vn.projectmanagement.common.swagger;

public class SwaggerMessages {
    // HTTP Status Messages
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
    public static final String CREATE_ROLE = "Create role";
    public static final String UPDATE_ROLE = "Update role";
    public static final String DELETE_ROLE = "Delete role";
    public static final String CREATE_ROLE_MESSAGE =  "{\"message\": \" Role created successfully \"}";
    public static final String UPDATE_ROLE_MESSAGE =  "{\"message\": \" Role updated successfully \"}";
    public static final String DELETE_ROLE_MESSAGE =  "{\"message\": \" Role deleted successfully \"}";
}
