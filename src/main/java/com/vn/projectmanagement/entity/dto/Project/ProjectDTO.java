package com.vn.projectmanagement.entity.dto.Project;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseUserDTO;
import com.vn.projectmanagement.entity.dto.BaseDTO.BaseTaskDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProjectDTO {
    private String title;
    private String description;
    private List<BaseTaskDTO> taskList;
    private List<BaseUserDTO> userList;
}
