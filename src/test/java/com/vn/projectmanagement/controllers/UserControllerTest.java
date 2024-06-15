package com.vn.projectmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.request.User.UpdatePasswordRequest;
import com.vn.projectmanagement.entity.request.User.UpdateUserRequest;
import com.vn.projectmanagement.factory.RoleFactory;
import com.vn.projectmanagement.factory.UserFactory;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleFactory roleFactory;

    @Autowired
    private UserFactory userFactory;

    private User user;

    @BeforeEach
    public void setUp() {
        Role role = roleFactory.create(1);
        for (int i = 0; i < 2; i++) {
            user = userFactory.create(role, i);
        }
    }

    @AfterEach
    public void tearDown() {
        roleFactory.delete();
        userFactory.delete();
    }

    /**
     * Test get all users.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Test get all users")
    void testGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/list")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[0].username").value("User 0"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data.[1].username").value("User 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.totalPages").value(1));
    }


    /**
     * Test get user by id.
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Test get user by id")
    void testGetUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/detail/{email}", user.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value(user.getPhone()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.OK));
    }

    /**
     * Test update user by username.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Test update user")
    void testUpdateUser() throws Exception {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("User 0 Updated");
        updateUserRequest.setEmail("test@example.com");
        updateUserRequest.setPhone("0123456789");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update/{username}", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateUserRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("User 0 Updated"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("test@example.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.phone").value("0123456789"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.UPDATE_USER))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    /**
     * Test change password.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Test change password")
    void testChangePassword() throws Exception {
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest();
        updatePasswordRequest.setOldPassword("123456");
        updatePasswordRequest.setNewPassword("12345678");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/update-password/{username}", user.getUsername())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatePasswordRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.CHANGE_PASSWORD));
    }
}
