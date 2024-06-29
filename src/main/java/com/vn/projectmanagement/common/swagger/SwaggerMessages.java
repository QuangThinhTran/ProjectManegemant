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
    public static final String INTERNAL_SERVER_ERROR_MESSAGE =
            "{\"message\": \"" + SwaggerMessages.INTERNAL_SERVER_ERROR + "\"}";

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
    public static final String CREATE_ROLE_MESSAGE = "{\"message\": \"" + SwaggerMessages.CREATE_ROLE + "\"}";
    public static final String UPDATE_ROLE_MESSAGE = "{\"message\": \"" + SwaggerMessages.UPDATE_ROLE + "\"}";
    public static final String DELETE_ROLE_MESSAGE = "{\"message\": \"" + SwaggerMessages.DELETE_ROLE + "\"}";

    // User Messages
    public static final String GET_ALL_USERS = "Get all users";
    public static final String GET_USER_BY_EMAIL = "Get user by email";
    public static final String CHANGE_PASSWORD = "Change password successfully";
    public static final String UPDATE_USER = "Update profile user successfully";
    public static final String CHANGE_PASSWORD_MESSAGE = "{\"message\": \"" + SwaggerMessages.CHANGE_PASSWORD + "\"}";
    public static final String UPDATE_USER_MESSAGE = "{\"message\": \"" + SwaggerMessages.UPDATE_USER + "\"}";
    // File Messages
    public static final String UPLOAD_FILE = "Upload file successfully";
    public static final String UPLOAD_FILE_MESSAGE = "{\"message\": \"" + SwaggerMessages.UPLOAD_FILE + "\"}";

    // Project Messages
    public static final String CREATE_PROJECT = "Create project successfully";
    public static final String UPDATE_PROJECT = "Update project successfully";
    public static final String DELETE_PROJECT = "Delete project successfully";
    public static final String GET_ALL_PROJECTS = "Get all projects";
    public static final String GET_PROJECT_BY_TITLE = "Get project by title";
    public static final String INVITE_STAFF = "Staff invited successfully to the project";
    public static final String REMOVE_STAFF = "Staff removed successfully from the project";
    public static final String CREATE_PROJECT_MESSAGE = "{\"message\": \"" + SwaggerMessages.CREATE_PROJECT + "\"}";
    public static final String UPDATE_PROJECT_MESSAGE = "{\"message\": \"" + SwaggerMessages.UPDATE_PROJECT + "\"}";
    public static final String DELETE_PROJECT_MESSAGE = "{\"message\": \"" + SwaggerMessages.DELETE_PROJECT + "\"}";
    public static final String INVITE_STAFF_MESSAGE = "{\"message\": \"" + SwaggerMessages.INVITE_STAFF + "\"}";
    public static final String REMOVE_STAFF_MESSAGE = "{\"message\": \"" + SwaggerMessages.REMOVE_STAFF + "\"}";

    // Task Messages
    public static final String CREATE_TASK = "Create task successfully";
    public static final String UPDATE_TASK = "Update task successfully";
    public static final String DELETE_TASK = "Delete task successfully";
    public static final String GET_ALL_TASKS = "Get all tasks";
    public static final String GET_TASK_BY_CODE = "Get task by code";
    public static final String UPDATE_STATUS_TASK = "Update status task successfully";
    public static final String ASSIGN_TASK = "Assign task to user successfully";
    public static final String CREATE_TASK_MESSAGE = "{\"message\": \"" + SwaggerMessages.CREATE_TASK + "\"}";
    public static final String UPDATE_TASK_MESSAGE = "{\"message\": \"" + SwaggerMessages.UPDATE_TASK + "\"}";
    public static final String DELETE_TASK_MESSAGE = "{\"message\": \"" + SwaggerMessages.DELETE_TASK + "\"}";
    public static final String UPDATE_STATUS_TASK_MESSAGE = "{\"message\": \"" + SwaggerMessages.UPDATE_STATUS_TASK + "\"}";
    public static final String ASSIGN_TASK_MESSAGE = "{\"message\": \"" + SwaggerMessages.ASSIGN_TASK + "\"}";
}
