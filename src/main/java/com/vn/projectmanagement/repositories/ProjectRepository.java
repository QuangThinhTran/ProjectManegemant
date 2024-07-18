package com.vn.projectmanagement.repositories;

import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    @Query("SELECT project FROM Project project " +
            "WHERE project.title LIKE %:title% " +
            "GROUP BY project.id, project.title, project.description"
    )
    Optional<Project> findByTitle(String title);

    Optional<Project> findById(UUID id);

    List<Project> findAllByUserList(User user);
}
