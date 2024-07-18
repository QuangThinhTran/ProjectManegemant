package com.vn.projectmanagement.mapped;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseProjectDTO;
import com.vn.projectmanagement.entity.dto.Project.ProjectDTO;
import com.vn.projectmanagement.mapped.interfaces.IProjectMapped;
import com.vn.projectmanagement.mapped.interfaces.ITaskMapped;
import com.vn.projectmanagement.mapped.interfaces.IUserMapped;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectMapped implements IProjectMapped {

    private final IUserMapped userMapped;
    private final ITaskMapped taskMapped;
    private final ProjectRepository projectRepository;

    @Autowired
    @Lazy
    public ProjectMapped(
            IUserMapped userMapped,
            ITaskMapped taskMapped,
            ProjectRepository projectRepository
    ) {
        this.userMapped = userMapped;
        this.taskMapped = taskMapped;
        this.projectRepository = projectRepository;
    }

    /**
     * Convert Page<Project> to Page<ProjectDTO>
     *
     * @param projects Page<Project>
     * @return Page<ProjectDTO>
     */
    @Override
    public Page<ProjectDTO> convertPageProjectToPageProjectDTO(Page<Project> projects) {
        return projects.map(this::mapProjectDTO);
    }

    /**
     * Map Project to ProjectDTO
     *
     * @param project Project
     * @return ProjectDTO
     */
    @Override
    public ProjectDTO mapProjectDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setTitle(project.getTitle());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setTaskList(taskMapped.convertListTaskToListBaseTaskDTO(project));
        projectDTO.setUserList(userMapped.convertListUserToListBaseUserDTO(project));
        return projectDTO;
    }

    /**
     * Map Project to BaseProjectDTO
     *
     * @param project Project
     * @return BaseProjectDTO
     */
    @Override
    public BaseProjectDTO mapBaseProjectDTO(Project project) {
        BaseProjectDTO baseProjectDTO = new BaseProjectDTO();
        baseProjectDTO.setTitle(project.getTitle());
        baseProjectDTO.setDescription(project.getDescription());
        return baseProjectDTO;
    }

    /**
     * Map Project to BaseProjectDTO
     *
     * @param user User
     * @return List<BaseProjectDTO>
     */
    @Override
    public List<BaseProjectDTO> convertListProjectToListBaseProjectDTO(User user) {
        List<BaseProjectDTO> userProjectDTOs = new ArrayList<>();
        projectRepository.findAllByUserList(user).forEach(this::mapBaseProjectDTO);
        return userProjectDTOs;
    }
}
