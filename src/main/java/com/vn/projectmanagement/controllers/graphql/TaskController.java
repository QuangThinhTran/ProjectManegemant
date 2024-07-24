package com.vn.projectmanagement.controllers.graphql;

import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.services.interfaces.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "TaskControllerGraphQL")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final TaskService taskService;

    @Autowired
    public TaskController(
            TaskService taskService
    ) {
        this.taskService = taskService;
    }

    @QueryMapping
    public Page<Task> getTasksByStatus(@Argument String status, @Argument int page, @Argument int size) {
        try {
            return taskService.listTasks(status, page, size);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping
    public Task getTaskByCode(@Argument String code) {
        try {
            return taskService.findTaskByCode(code);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
