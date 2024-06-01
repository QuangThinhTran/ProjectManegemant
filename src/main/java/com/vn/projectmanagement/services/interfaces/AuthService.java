package com.vn.projectmanagement.services.interfaces;

import com.vn.projectmanagement.entity.dto.AuthenticationDTO;
import com.vn.projectmanagement.entity.request.Auth.LoginRequest;
import com.vn.projectmanagement.entity.request.Auth.RegisterRequest;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;

public interface AuthService {
    AuthenticationDTO createUser(RegisterRequest registerRequest, Role role);

    void login(LoginRequest loginRequest);

    String generateToken(AuthenticationDTO user);

    void checkPasswordMatched(String password, String confirmPassword);

    AuthenticationDTO mapAuthenticationDTO(User user);

    void logout();
}
