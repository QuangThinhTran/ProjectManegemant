package com.vn.projectmanagement.services;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.entity.request.Task.TaskRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.TaskRepository;
import com.vn.projectmanagement.services.email.MailService;
import com.vn.projectmanagement.services.interfaces.ProjectService;
import com.vn.projectmanagement.services.interfaces.TaskService;
import com.vn.projectmanagement.services.interfaces.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final UserService userService;
    private final MailService mailService;

    @Autowired
    public TaskServiceImpl(
            TaskRepository taskRepository,
            UserService userService,
            ProjectService projectService,
            MailService mailService
    ) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectService = projectService;
        this.mailService = mailService;
    }

    /**
     * Get a page of tasks
     *
     * @param status task status
     * @param page   page number
     * @param size   number of tasks per page
     * @return a page of tasks
     */
    @Override
    public Page<Task> listTasks(String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

//        if (!status.isBlank()) {
//            return taskRepository.getTasksByStatus(status, pageable);
//        }
        System.out.println(123);
        return taskRepository.findAll(pageable);
    }

    /**
     * Find a task by task code
     *
     * @param taskCode task code
     * @return a task
     */
    @Override
    public Task findTaskByCode(String taskCode) {
        return taskRepository.findByTaskCode(taskCode).orElseThrow(() -> new ApiRequestException(
                taskCode + ValidationConstants.IS_NOT_FOUND,
                HttpStatus.NOT_FOUND
        ));
    }


    /**
     * Create a task
     *
     * @param taskRequest task request
     */
    @Override
    public void createTask(TaskRequest taskRequest) {
        String taskCode = RandomStringUtils.randomAlphanumeric(8);
        Task task = new Task();
        setUpTask(task, taskRequest, taskCode);
    }

    /**
     * Update a task
     *
     * @param taskCode    task code
     * @param taskRequest task request
     */
    @Override
    public void updateTask(String taskCode, TaskRequest taskRequest) {
        System.out.println(taskRequest);
        Task task = findTaskByCode(taskCode);
        System.out.println(task);
        setUpTask(task, taskRequest, task.getTaskCode());
    }

    /**
     * Delete a task
     *
     * @param task task code
     */
    @Override
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    /**
     * Assign a task to a user
     *
     * @param task task
     * @param user user
     */
    @Override
    public void assignTaskToUser(Task task, User user) {
        task.setUser(user);
        taskRepository.save(task);
    }

    /**
     * Update task status
     *
     * @param task   task
     * @param status task status
     */
    @Override
    public void updateStatus(Task task, String status) {
        task.setStatus(status);
        taskRepository.save(task);
    }

    /**
     * Send mail to update task status
     *
     * @param email   email
     * @param project project
     * @param task    task
     */
    @Override
    public void sendMailUpdateStatus(String email, Project project, Task task) {
        Map<String, Object> attributes = Map.of(
                "task", task,
                "project", project
        );

        mailService.sendMail(email, "update-status-task", attributes);
    }


    /**
     * Set up task
     *
     * @param task        task
     * @param taskRequest task request
     */
    private void setUpTask(Task task, TaskRequest taskRequest, String taskCode) {
        Project project = projectService.getProjectById(taskRequest.getProjectID());
        User user = userService.findByEmail(taskRequest.getUserEmail());

        task.setTaskCode(taskCode);
        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setStatus(taskRequest.getStatus());
        task.setExpirationDate(taskRequest.getExpirationDate());
        task.setProject(project);
        task.setUser(user);
        taskRepository.save(task);
    }
}
