package com.vn.projectmanagement.controllers.graphql;

import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.services.interfaces.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "ProjectControllerGraphQL")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final ProjectService projectService;

    @Autowired
    public ProjectController(
            ProjectService projectService
    ) {
        this.projectService = projectService;
    }

    @QueryMapping
    public Page<Project> findAllProjects(@Argument int page, @Argument int size) {
        try {
            return projectService.listProjects(page, size);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping
    public Project findProjectByTitle(@Argument String title) {
        try {
            return projectService.getProjectByTitle(title);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
