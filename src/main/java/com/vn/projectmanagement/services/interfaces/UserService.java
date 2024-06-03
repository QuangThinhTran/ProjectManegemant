package com.vn.projectmanagement.services.interfaces;

import com.vn.projectmanagement.entity.request.Auth.RegisterRequest;
import com.vn.projectmanagement.entity.request.User.UpdatePasswordRequest;
import com.vn.projectmanagement.entity.request.User.UpdateUserRequest;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User findByUsername(String username);

    User findByEmail(String email);

    User create(RegisterRequest user, Role role);

    void checkUsernameExist(String username);

    void checkEmailExist(String email);

    void checkPhoneExist(String phone);

    Page<User> filterUsers(Pageable pageable, int page, int size);

    User update(String username, UpdateUserRequest updateUserRequest);

    void updatePassword(String username, UpdatePasswordRequest updatePasswordRequest);
}
