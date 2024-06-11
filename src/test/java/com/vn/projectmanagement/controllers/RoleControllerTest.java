package com.vn.projectmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.projectmanagement.common.constants.ExceptionConstant;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.request.Role.CreateRoleRequest;
import com.vn.projectmanagement.entity.request.Role.UpdateRoleRequest;
import com.vn.projectmanagement.factory.RoleFactory;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.repositories.RoleRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
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
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleFactory roleFactory;

    private Role role;

    @BeforeEach
    public void setUp() {
        for (int i = 0; i < 2; i++) {
            role = roleFactory.create(i);
        }
    }

    @AfterEach
    public void tearDown() {
        roleRepository.deleteAll();
    }

    /**
     * Test get all roles.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Test get all roles")
    void testGetAllRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/role/all"))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Role 0")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Role 1")));
    }

    /**
     * Test create role.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Create role")
    void testCreateRole() throws Exception {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName("Role 3");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/role/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoleRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.CREATE_ROLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CREATED.value()));
    }

    /**
     * Test create role with existed name.
     *
     * @throws Exception - the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Create role with existed name")
    void testCreateRoleWithExistName() throws Exception {
        CreateRoleRequest createRoleRequest = new CreateRoleRequest();
        createRoleRequest.setName(role.getName());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/role/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionConstant.ROLE_EXISTED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Test update role.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Update role")
    void testUpdateRole() throws Exception {
        UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest();
        updateRoleRequest.setOldName(role.getName());
        updateRoleRequest.setNewName(RandomStringUtils.randomNumeric(10));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/role/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRoleRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.UPDATE_ROLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()));
    }

    /**
     * Test update role with existed name.
     *
     * @throws Exception - the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Update role with existed name")
    void testUpdateRoleWithExistName() throws Exception {
        UpdateRoleRequest updateRoleRequest = new UpdateRoleRequest();
        updateRoleRequest.setOldName(role.getName());
        updateRoleRequest.setNewName(role.getName());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/role/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRoleRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(ExceptionConstant.ROLE_EXISTED))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Test delete role.
     *
     * @throws Exception the exception
     */
    @Test
    @WithMockUser
    @DisplayName("Delete role")
    void testDeleteRole() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/role/delete/{roleName}", role.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.DELETE_ROLE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()));
    }
}
