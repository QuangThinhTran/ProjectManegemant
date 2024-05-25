package com.vn.projectmanagement.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@Table(name = "tasks")
public class Task extends BaseModel{
    @Column(name = "task_code")
    private String taskCode;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "expiration_date")
    private Date expirationDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "project_id")
    private List<Project> projectList;

    @ManyToOne(cascade = CascadeType.ALL)
    @Column(name = "user_id")
    private List<User> userList;

    public void setTaskCode() {
        Random random = new Random(6);
        this.taskCode = random.toString();
    }
}
