package com.vn.projectmanagement.mapped.interfaces;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseTaskDTO;
import com.vn.projectmanagement.entity.dto.Task.TaskDTO;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ITaskMapped {

    TaskDTO mapTaskDTO(Task task);

    List<BaseTaskDTO> convertListTaskToListBaseTaskDTO(Project project);

    List<BaseTaskDTO> convertListTaskToListBaseTaskDTO(User user);

    Page<TaskDTO> convertPageTaskToPageTaskDTO(Page<Task> tasks);
}
