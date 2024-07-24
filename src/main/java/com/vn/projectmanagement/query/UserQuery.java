package com.vn.projectmanagement.query;

import com.vn.projectmanagement.entity.dto.User.UserDTO;
import com.vn.projectmanagement.mapped.UserMapped;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class UserQuery {
    private final UserService userService;
    private final UserMapped userMapped;

    @Autowired
    public UserQuery(
            UserService userService,
            UserMapped userMapped
    ) {
        this.userService = userService;
        this.userMapped = userMapped;
    }

    public Page<UserDTO> findAllUsers(int page, int size) {
        Page<User> usersPage = userService.filterUsers(page, size);
        return userMapped.convertPageUserToPageUserDTO(usersPage);
    }

    public UserDTO findUserByEmail(String email) {
        User user = userService.findByEmail(email);
        return userMapped.mapUserDTO(user);
    }
}
