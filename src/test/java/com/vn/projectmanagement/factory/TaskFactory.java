package com.vn.projectmanagement.factory;

import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.TaskRepository;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TaskFactory {

    private final TaskRepository taskRepository;

    public TaskFactory(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task create(int i, User user, Project project) {
        Task task = new Task();
        task.setTaskCode("Task " + i);
        task.setTitle("Title " + i);
        task.setDescription("Description " + i);
        task.setExpirationDate(new Date());
        task.setStatus("todo");
        task.setUser(user);
        task.setProject(project);
        return taskRepository.save(task);
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }
}
