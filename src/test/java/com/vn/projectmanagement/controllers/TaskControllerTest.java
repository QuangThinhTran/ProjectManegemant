package com.vn.projectmanagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.request.Task.AssignTaskRequest;
import com.vn.projectmanagement.entity.request.Task.TaskRequest;
import com.vn.projectmanagement.entity.request.Task.UpdateStatusTaskRequest;
import com.vn.projectmanagement.factory.ProjectFactory;
import com.vn.projectmanagement.factory.RoleFactory;
import com.vn.projectmanagement.factory.TaskFactory;
import com.vn.projectmanagement.factory.UserFactory;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.Task;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskFactory taskFactory;

    @Autowired
    private UserFactory userFactory;

    @Autowired
    private RoleFactory roleFactory;

    @Autowired
    private ProjectFactory projectFactory;

    private User user;
    private Project project;
    private Task task;

    @BeforeEach
    public void setUp() {
        Role role = roleFactory.create(1);
        user = userFactory.create(role, 1);
        project = projectFactory.createProject(1);
        for (int i = 1; i <= 2; i++) {
            task = taskFactory.create(i, user, project);
        }
    }

    @AfterEach
    public void tearDown() {
        taskFactory.deleteAll();
        projectFactory.deleteAll();
        userFactory.delete();
        roleFactory.delete();
    }

    @Test
    public void testListTasks() {
        // test listTasks
    }

    /**
     * Test create task
     *
     * @throws Exception Test create task
     */
    @Test
    @DisplayName("Test create task")
    @WithMockUser
    public void testCreateTask() throws Exception {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Task 3");
        taskRequest.setDescription("Task 3 description");
        taskRequest.setStatus("todo");
        taskRequest.setExpirationDate(new Date());
        taskRequest.setProjectID(project.getId());
        taskRequest.setUserEmail(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/task/create").contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.CREATE_TASK));
    }

    /**
     * Test update task
     *
     * @throws Exception Test update task
     */
    @Test
    @DisplayName("Test update task")
    @WithMockUser
    public void testUpdateTask() throws Exception {
        TaskRequest taskRequest = new TaskRequest();
        taskRequest.setTitle("Task 3");
        taskRequest.setDescription("Task 3 description");
        taskRequest.setStatus("todo");
        taskRequest.setExpirationDate(new Date());
        taskRequest.setProjectID(project.getId());
        taskRequest.setUserEmail(user.getEmail());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/task/update/" + task.getTaskCode())
                        .contentType("application/json").content(objectMapper.writeValueAsString(taskRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.UPDATE_TASK));
    }

    /**
     * Test assign task
     *
     * @throws Exception Test assign task
     */
    @Test
    @DisplayName("Test assign task")
    @WithMockUser
    public void testAssignTask() throws Exception {
        AssignTaskRequest assignTaskRequest = new AssignTaskRequest();
        assignTaskRequest.setTaskCode(task.getTaskCode());

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/task/assign-task/" + user.getEmail())
                        .contentType("application/json").content(objectMapper.writeValueAsString(assignTaskRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.ASSIGN_TASK));
    }

    /**
     * Test update status
     *
     * @throws Exception Test update status
     */
    @Test
    @DisplayName("Test update status")
    @WithMockUser
    public void testUpdateStatus() throws Exception {
        UpdateStatusTaskRequest updateStatusTaskRequest = new UpdateStatusTaskRequest();
        updateStatusTaskRequest.setStatus("done");

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/task/update-status/" + task.getTaskCode())
                        .contentType("application/json").content(objectMapper.writeValueAsString(updateStatusTaskRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.UPDATE_STATUS_TASK));
    }

    /**
     * Test delete task
     *
     * @throws Exception Test delete task
     */
    @Test
    @DisplayName("Test delete task")
    @WithMockUser
    public void testDeleteTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/task/delete/" + task.getTaskCode()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(SwaggerMessages.DELETE_TASK));
    }
}
