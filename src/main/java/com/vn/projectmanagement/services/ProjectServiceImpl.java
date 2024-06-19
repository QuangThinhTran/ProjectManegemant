package com.vn.projectmanagement.services;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.entity.request.Project.ProjectRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.repositories.ProjectRepository;
import com.vn.projectmanagement.services.interfaces.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * List all projects
     *
     * @param pageable - pageable
     * @param page     - page
     * @param size     - size
     * @return Page<Project>
     */
    @Override
    public Page<Project> listProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.findAll(pageable);
    }

    /**
     * Create a new project
     *
     * @param createProjectRequest - createProjectRequest
     */
    @Override
    public void createProject(ProjectRequest createProjectRequest) {
        Project project = new Project();
        project.setTitle(createProjectRequest.getTitle());
        project.setDescription(createProjectRequest.getDescription());
        projectRepository.save(project);
    }

    /**
     * Find a specific record of Project by id
     *
     * @param id - id
     * @return Project
     */
    @Override
    public Project getProjectById(UUID id) {
        return projectRepository.findById(id).orElseThrow(() -> new ApiRequestException(
                Project.class.getSimpleName() + ValidationConstants.IS_NOT_FOUND,
                HttpStatus.NOT_FOUND
        ));
    }

    /**
     * Find a specific record of Project by title
     *
     * @param title - title
     * @return Project
     */
    @Override
    public Project getProjectByTitle(String title) {
        return projectRepository.findByTitle(title).orElseThrow(() -> new ApiRequestException(
                Project.class.getSimpleName() + ValidationConstants.IS_NOT_FOUND,
                HttpStatus.NOT_FOUND
        ));
    }

    /**
     * Update a specific record of Project by id within provided data
     *
     * @param id             - id
     * @param projectRequest - projectRequest
     */
    @Override
    public void updateProject(UUID id, ProjectRequest projectRequest) {
        Project project = getProjectById(id);
        project.setTitle(projectRequest.getTitle());
        project.setDescription(projectRequest.getDescription());
        projectRepository.save(project);
    }

    /**
     * Delete a specific record of Project
     *
     * @param project - project
     */
    @Override
    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }
}
