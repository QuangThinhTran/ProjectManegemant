package com.vn.projectmanagement.mapped;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseTaskDTO;
import com.vn.projectmanagement.entity.dto.Task.TaskDTO;
import com.vn.projectmanagement.mapped.interfaces.IProjectMapped;
import com.vn.projectmanagement.mapped.interfaces.ITaskMapped;
import com.vn.projectmanagement.mapped.interfaces.IUserMapped;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapped implements ITaskMapped {

    private final IProjectMapped projectMapped;
    private final IUserMapped userMapped;
    private final TaskRepository taskRepository;

    @Autowired
    @Lazy
    public TaskMapped(
            IProjectMapped projectMapped,
            IUserMapped userMapped,
            TaskRepository taskRepository
    ) {
        this.projectMapped = projectMapped;
        this.userMapped = userMapped;
        this.taskRepository = taskRepository;
    }

    /**
     * Map Task to TaskDTO
     *
     * @param task Task
     * @return TaskDTO
     */
    @Override
    public BaseTaskDTO mapBaseTaskDTO(Task task) {
        BaseTaskDTO baseTaskDTO = new BaseTaskDTO();
        baseTaskDTO.setTaskCode(task.getTaskCode());
        baseTaskDTO.setTitle(task.getTitle());
        baseTaskDTO.setDescription(task.getDescription());
        baseTaskDTO.setStatus(task.getStatus());
        baseTaskDTO.setExpirationDate(task.getExpirationDate());
        return baseTaskDTO;
    }

    /**
     * Map Task to TaskDTO
     *
     * @param task Task
     * @return TaskDTO
     */
    @Override
    public TaskDTO mapTaskDTO(Task task)
    {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskCode(task.getTaskCode());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setExpirationDate(task.getExpirationDate());
        taskDTO.setBaseProjectDTO(projectMapped.mapBaseProjectDTO(task.getProject()));
        taskDTO.setBaseUserDTO(userMapped.mapBaseUserDTO(task.getUser()));
        return taskDTO;
    }

    /**
     * Map Task to BaseTaskDTO
     *
     * @param project Project
     * @return List<BaseTaskDTO>
     */
    @Override
    public List<BaseTaskDTO> convertListTaskToListBaseTaskDTO(Project project) {
        List<BaseTaskDTO> baseTaskDTOList = new ArrayList<>();
        taskRepository.findAllByProject(project).forEach(task -> baseTaskDTOList.add(mapBaseTaskDTO(task)));
        return baseTaskDTOList;
    }

    /**
     * Map Task to BaseTaskDTO
     *
     * @param user User
     * @return List<BaseTaskDTO>
     */
    @Override
    public List<BaseTaskDTO> convertListTaskToListBaseTaskDTO(User user) {
        List<BaseTaskDTO> userTaskDTOList = new ArrayList<>();
        taskRepository.findAllByUser(user).forEach(this::mapTaskDTO);
        return userTaskDTOList;
    }

    /**
     * Convert Page<Task> to Page<TaskDTO>
     *
     * @param tasks Page<Task>
     * @return Page<TaskDTO>
     */
    @Override
    public Page<TaskDTO> convertPageTaskToPageTaskDTO(Page<Task> tasks) {
        return tasks.map(this::mapTaskDTO);
    }
}
