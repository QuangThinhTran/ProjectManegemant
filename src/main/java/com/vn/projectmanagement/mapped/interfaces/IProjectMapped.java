package com.vn.projectmanagement.mapped.interfaces;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseProjectDTO;
import com.vn.projectmanagement.entity.dto.Project.ProjectDTO;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProjectMapped {

    ProjectDTO mapProjectDTO(Project project);

    Page<ProjectDTO> convertPageProjectToPageProjectDTO(Page<Project> projects);

    BaseProjectDTO mapBaseProjectDTO(Project project);

    List<BaseProjectDTO> convertListProjectToListBaseProjectDTO(User user);
}
