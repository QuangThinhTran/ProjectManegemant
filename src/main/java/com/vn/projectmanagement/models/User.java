package com.vn.projectmanagement.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.projectmanagement.repositories.ImageRepository;
import com.vn.projectmanagement.common.utils.SpringContext;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseModel {
    @Column(name = "username")
    private String username;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<Task> taskList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projectList;

    public String getRole() {
        return role.getName();
    }

    @Getter
    @Transient
    private String avatar;

    @PostLoad
    private void loadAvatar() {
        ImageRepository imageRepository = SpringContext.getBean(ImageRepository.class);
        Image avatar = imageRepository.findByEntityIdAndEntityType(this.getId(), this.getClass().getSimpleName());
        this.avatar = avatar != null ? avatar.getPath() : null;
    }
}
