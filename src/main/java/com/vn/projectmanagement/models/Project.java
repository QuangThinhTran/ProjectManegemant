package com.vn.projectmanagement.models;

import com.fasterxml.jackson.annotation.JsonView;
import com.vn.projectmanagement.entity.view.View;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "projects")
@JsonView({View.UserView.class, View.ProjectView.class})
public class Project extends BaseModel{
    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Task> taskList;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> userList;
}
