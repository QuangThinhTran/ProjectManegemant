package com.vn.projectmanagement.services.interfaces;

import com.vn.projectmanagement.entity.request.Project.ProjectRequest;
import com.vn.projectmanagement.models.Project;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface ProjectService {
    Page<Project> listProjects(int page, int size);

    void createProject(ProjectRequest projectRequest);

    Project getProjectById(UUID id);

    Project getProjectByTitle(String title);

    void updateProject(UUID id ,ProjectRequest projectRequest);

    void deleteProject(Project project);
}
