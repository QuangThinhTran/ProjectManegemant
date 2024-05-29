package com.vn.projectmanagement.services.interfaces;

import com.vn.projectmanagement.entity.request.RegisterRequest;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;

public interface UserService {
    User findByUsername(String username);
    User create(RegisterRequest user, Role role);
    void checkUsernameExist(String username);
    void checkEmailExist(String email);
    void checkPhoneExist(String phone);
}
