package com.vn.projectmanagement.entity.dto.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.projectmanagement.entity.dto.BaseDTO.BaseProjectDTO;
import com.vn.projectmanagement.entity.dto.BaseDTO.BaseUserDTO;
import com.vn.projectmanagement.entity.dto.User.UserDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskDTO {
    private String taskCode;
    private String title;
    private String description;
    private String status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expirationDate;
    private BaseProjectDTO baseProjectDTO;
    private BaseUserDTO baseUserDTO;
}
