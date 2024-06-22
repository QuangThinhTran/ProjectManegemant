package com.vn.projectmanagement.services;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.entity.request.Project.ProjectRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.ProjectRepository;
import com.vn.projectmanagement.services.email.MailService;
import com.vn.projectmanagement.services.interfaces.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MailService mailService;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository, MailService mailService) {
        this.projectRepository = projectRepository;
        this.mailService = mailService;
    }

    /**
     * List all projects
     *
     * @param page - page
     * @param size - size
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

    /**
     * Invite staff to a project
     *
     * @param user    - user
     * @param project - project
     * @return boolean
     */
    @Override
    public boolean inviteStaffToProject(User user, Project project) {
        List<User> userList = project.getUserList();

        if (userList == null) {
            userList = new ArrayList<>();
        }

        if (userList.contains(user)) {
            return false;
        }
        userList.add(user);

        project.setUserList(userList);
        projectRepository.save(project);
        return true;
    }

    /**
     * Remove staff from a project
     *
     * @param user    - user
     * @param project - project
     * @return boolean
     */
    @Override
    public boolean removeStaffFromProject(User user, Project project) {
        List<User> userList = project.getUserList();

        if (!userList.contains(user)) {
            return false;
        }
        userList.remove(user);

        projectRepository.save(project);
        return true;
    }

    /**
     * Send mail to invited staff
     *
     * @param email       - email
     * @param project - projectName
     */
    @Override
    public void sendMailInvitedStaff(String email, Project project) {
        Map<String, Object> attributes = Map.of(
                "email", email,
                "project", project
        );

        mailService.sendMail(email, "invite-project", attributes);
    }
}
