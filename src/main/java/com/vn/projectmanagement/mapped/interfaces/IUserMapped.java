package com.vn.projectmanagement.mapped.interfaces;

import com.vn.projectmanagement.entity.dto.BaseDTO.BaseUserDTO;
import com.vn.projectmanagement.entity.dto.User.UserDTO;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserMapped {
    Page<UserDTO> convertPageUserToPageUserDTO(Page<User> users);

    UserDTO mapUserDTO(User user);

    List<BaseUserDTO> convertListUserToListBaseUserDTO(Project project);

    BaseUserDTO mapBaseUserDTO(User user);
}
