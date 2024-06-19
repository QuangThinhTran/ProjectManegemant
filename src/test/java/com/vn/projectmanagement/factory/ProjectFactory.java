package com.vn.projectmanagement.factory;

import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.repositories.ProjectRepository;
import org.springframework.stereotype.Component;

@Component
public class ProjectFactory {

    private final ProjectRepository projectRepository;

    public ProjectFactory(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project createProject(int i) {
        Project project = new Project();
        project.setTitle("Project " + i);
        project.setDescription("Description " + i);
        return projectRepository.save(project);
    }

    public void deleteAll() {
        projectRepository.deleteAll();
    }
}
