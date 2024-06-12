package com.vn.projectmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.projectmanagement.common.constants.ExceptionConstant;
import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.request.Auth.LoginRequest;
import com.vn.projectmanagement.entity.request.Auth.RegisterRequest;
import com.vn.projectmanagement.factory.RoleFactory;
import com.vn.projectmanagement.factory.UserFactory;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.RoleRepository;
import com.vn.projectmanagement.repositories.UserRepository;
import com.vn.projectmanagement.services.interfaces.AuthService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private RoleFactory roleFactory;

    private String password;
    private Role role;

    @BeforeEach
    public void setUp() {
        this.password = "123456";

        role = roleFactory.create(1);
    }

    @AfterEach
    public void tearDown() {
        roleFactory.delete();
        userFactory.delete();
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should register successfully")
    void shouldRegisterSuccessfully() throws Exception {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("test");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPhone("1234567890");
        registerRequest.setPassword(password);
        registerRequest.setConfirmPassword(password);
        registerRequest.setRole(role.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(SwaggerMessages.REGISTRATION_SUCCESS_MESSAGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(registerRequest.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(registerRequest.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value(registerRequest.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(role.getName()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should register failed return an error message when the username already exists")
    void shouldRegisterFailedReturnErrorMessageWhenUsernameAlreadyExists() throws Exception {
        User user = userFactory.create(role, 1);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(user.getUsername());
        registerRequest.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        registerRequest.setPhone(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setPassword(password);
        registerRequest.setConfirmPassword(password);
        registerRequest.setRole(role.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(registerRequest.getUsername() + ValidationConstants.IS_EXISTED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should register failed return an error message when the email already exists")
    void shouldRegisterFailedReturnErrorMessageWhenEmailAlreadyExists() throws Exception {
        User user = userFactory.create(role, 1);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setEmail(user.getEmail());
        registerRequest.setPhone(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setPassword(password);
        registerRequest.setConfirmPassword(password);
        registerRequest.setRole(role.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(registerRequest.getEmail() + ValidationConstants.IS_EXISTED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should register failed return an error message when the phone already exists")
    void shouldRegisterFailedReturnErrorMessageWhenPhoneAlreadyExists() throws Exception {
        User user = userFactory.create(role, 1);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        registerRequest.setPhone(user.getPhone());
        registerRequest.setPassword(password);
        registerRequest.setConfirmPassword(password);
        registerRequest.setRole(role.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(registerRequest.getPhone() + ValidationConstants.IS_EXISTED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should register failed return an error message when the password and confirm password do not match")
    void shouldRegisterFailedReturnErrorMessageWhenPasswordAndConfirmPasswordDoNotMatch() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setEmail(RandomStringUtils.randomAlphanumeric(10) + "@example.com");
        registerRequest.setPhone(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setPassword(password);
        registerRequest.setConfirmPassword(RandomStringUtils.randomAlphanumeric(10));
        registerRequest.setRole(role.getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ExceptionConstant.PASSWORD_AND_PASSWORD_CONFIRM_NOT_MATCHED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should login successfully")
    void shouldLoginSuccessfully() throws Exception {
        User user = userFactory.create(role, 1);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(password);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(SwaggerMessages.LOGIN_SUCCESS_MESSAGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value(user.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.role").value(role.getName()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should login failed return an error message when the username is not valid")
    void shouldLoginFailedReturnErrorMessageWhenUsernameIsNotValid() throws Exception {
        User user = userFactory.create(role, 1);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(RandomStringUtils.randomAlphanumeric(10));
        loginRequest.setPassword(user.getPassword());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(ExceptionConstant.USER_NOT_FOUND))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()));
    }

    /**
     * Test for the login method in the AuthController class
     *
     * @throws Exception - if an error occurs during the process
     */
    @Test
    @DisplayName("Should login failed return an error message when the password is not valid")
    void shouldLoginFailedReturnErrorMessageWhenPasswordIsNotValid() throws Exception {
        User user = userFactory.create(role, 1);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(RandomStringUtils.randomAlphanumeric(10));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message")
                        .value(SwaggerMessages.LOGIN_FAILURE_MESSAGE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }
}