package com.vn.projectmanagement.mapped;

import com.vn.projectmanagement.mapped.interfaces.IProjectMapped;
import com.vn.projectmanagement.mapped.interfaces.ITaskMapped;
import com.vn.projectmanagement.mapped.interfaces.IUserMapped;
import com.vn.projectmanagement.entity.dto.BaseDTO.BaseUserDTO;
import com.vn.projectmanagement.entity.dto.User.UserDTO;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapped implements IUserMapped {
    private final UserRepository userRepository;
    private final IProjectMapped projectMapped;
    private final ITaskMapped taskMapped;

    @Autowired
    @Lazy
    public UserMapped(
            UserRepository userRepository,
            IProjectMapped projectMapped,
            ITaskMapped taskMapped
    ) {
        this.userRepository = userRepository;
        this.projectMapped = projectMapped;
        this.taskMapped = taskMapped;
    }

    /**
     * Map User to UserDTO
     *
     * @param user User
     * @return UserDTO
     */
    @Override
    public UserDTO mapUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setRole(user.getRole());
        userDTO.setAvatar(user.getAvatar());
        userDTO.setTaskList(taskMapped.convertListTaskToListBaseTaskDTO(user));
        userDTO.setProjectList(projectMapped.convertListProjectToListBaseProjectDTO(user));
        return userDTO;
    }

    /**
     * Map User to BaseUserDTO
     *
     * @param user User
     * @return BaseUserDTO
     */
    @Override
    public BaseUserDTO mapBaseUserDTO(User user) {
        BaseUserDTO baseUserDTO = new BaseUserDTO();
        baseUserDTO.setUsername(user.getUsername());
        baseUserDTO.setEmail(user.getEmail());
        baseUserDTO.setPhone(user.getPhone());
        baseUserDTO.setRole(user.getRole());
        baseUserDTO.setAvatar(user.getAvatar());
        return baseUserDTO;
    }

    /**
     * Convert Page<User> to Page<UserDTO>
     *
     * @param users Page<User>
     * @return Page<UserDTO>
     */
    @Override
    public Page<UserDTO> convertPageUserToPageUserDTO(Page<User> users) {
        return users.map(this::mapUserDTO);
    }

    /**
     * Map User to BaseUserDTO
     *
     * @param project Project
     * @return List<BaseUserDTO>
     */
    public List<BaseUserDTO> convertListUserToListBaseUserDTO(Project project) {
        List<BaseUserDTO> projectUserDTOs = new ArrayList<>();
        userRepository.findAllByProjectList(project).forEach(user -> projectUserDTOs.add(mapBaseUserDTO(user)));
        return projectUserDTOs;
    }
}
