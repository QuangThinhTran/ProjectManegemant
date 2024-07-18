package com.vn.projectmanagement.entity.dto.User;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseProjectDTO;
import com.vn.projectmanagement.entity.dto.BaseDTO.BaseTaskDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String username;
    private String phone;
    private String email;
    private String role;
    private String avatar;
    private List<BaseTaskDTO> taskList;
    private List<BaseProjectDTO> projectList;
}
