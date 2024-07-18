package com.vn.projectmanagement.repositories;

import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    @Query("SELECT task FROM Task task WHERE task.taskCode = :taskCode")
    Optional<Task> findByTaskCode(String taskCode);

    @Query("SELECT task FROM Task task WHERE task.status = :status")
    Page<Task> getTasksByStatus(String status, Pageable pageable);

    List<Task> findAllByUser(User user);

    List<Task> findAllByProject(Project project);
}
