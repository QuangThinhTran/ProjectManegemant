package com.vn.projectmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.request.Project.ProjectRequest;
import com.vn.projectmanagement.factory.ProjectFactory;
import com.vn.projectmanagement.models.Project;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProjectFactory projectFactory;

    private Project project;

    @BeforeEach
    public void setUp() {
        for (int i = 1; i <= 2; i++) {
            project = projectFactory.createProject(i);
        }
    }

    @AfterEach
    public void tearDown() {
        projectFactory.deleteAll();
    }

    /**
     * Test get all projects
     *
     * @throws Exception Test get all projects
     */
    @Test
    @DisplayName("Test get all projects")
    @WithMockUser
    public void testGetAllProjects() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/project/list"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].title").value("Project 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description").value("Description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].title").value("Project 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].description").value("Description 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.size").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.number").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.totalElements").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.page.totalPages").value(1));
    }

    /**
     * Test get project by Title
     *
     * @throws Exception Test get project by id
     */
    @Test
    @DisplayName("Test get project by id")
    @WithMockUser
    public void testGetProjectByTitle() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/project/detail/" + project.getTitle()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.title").value(project.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value(project.getDescription()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.OK));;
    }

    /**
     * Test create project
     *
     * @throws Exception Test create project
     */
    @Test
    @DisplayName("Test create project")
    @WithMockUser
    public void testCreateProject() throws Exception {
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setTitle("Project 3");
        projectRequest.setDescription("Description 3");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/project/create")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.CREATE_PROJECT));
    }

    /**
     * Test update project
     *
     * @throws Exception Test update project
     */
    @Test
    @DisplayName("Test update project")
    @WithMockUser
    public void testUpdateProject() throws Exception {
        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setTitle("Project 3");
        projectRequest.setDescription("Description 3");

        mockMvc.perform(MockMvcRequestBuilders.put("/api/project/update/" + this.project.getId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(projectRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.UPDATE_PROJECT));
    }

    /**
     * Test delete project
     *
     * @throws Exception Test delete project
     */
    @Test
    @DisplayName("Test delete project")
    @WithMockUser
    public void testDeleteProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/project/delete/" + this.project.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.DELETE_PROJECT));
    }
}
