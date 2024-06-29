package com.vn.projectmanagement.services.interfaces;

import com.vn.projectmanagement.entity.request.Task.TaskRequest;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;

public interface TaskService {
    Page<Task> listTasks(String status, int page, int size);

    Task findTaskByCode(String taskCode);

    void createTask(TaskRequest taskRequest);

    void updateTask(String taskCode, TaskRequest taskRequest);

    void deleteTask(Task task);

    void assignTaskToUser(Task task, User user);

    void updateStatus(Task task, String status);

    void sendMailUpdateStatus(String email, Project project, Task task);
}
