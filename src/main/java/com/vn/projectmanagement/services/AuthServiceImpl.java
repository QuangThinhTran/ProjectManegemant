package com.vn.projectmanagement.services;

import com.vn.projectmanagement.common.constants.ExceptionConstant;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.dto.AuthenticationDTO;
import com.vn.projectmanagement.entity.request.Auth.LoginRequest;
import com.vn.projectmanagement.entity.request.Auth.RegisterRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.security.JWT.JwtTokenUtil;
import com.vn.projectmanagement.services.interfaces.AuthService;
import com.vn.projectmanagement.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthServiceImpl(
            UserService userService,
            JwtTokenUtil jwtTokenUtil,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Create a newly record of User within provided register request and role
     *
     * @param registerRequest - register request
     * @param role            - role
     * @return user
     */
    @Override
    public AuthenticationDTO createUser(RegisterRequest registerRequest, Role role) {
        return mapAuthenticationDTO(this.userService.create(registerRequest, role));
    }

    /**
     * Handle login
     *
     * @param loginRequest - login request
     */
    @Override
    public void login(LoginRequest loginRequest) {
        try {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            Authentication authenticated = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticated);
        } catch (Exception e) {
            throw new ApiRequestException(SwaggerMessages.LOGIN_FAILURE_MESSAGE, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Handle Logout
     */
    @Override
    public void logout() {
        SecurityContextHolder.clearContext();
    }

    /**
     * Generate token
     *
     * @param user - user
     * @return token
     */
    @Override
    public String generateToken(AuthenticationDTO user) {
        return jwtTokenUtil.generateToken(user);
    }

    /**
     * Check if password matched
     *
     * @param password        - password
     * @param confirmPassword - confirm password
     */
    @Override
    public void checkPasswordMatched(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new ApiRequestException(
                    ExceptionConstant.PASSWORD_AND_PASSWORD_CONFIRM_NOT_MATCHED,
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Map authentication DTO
     *
     * @param user - user
     * @return authentication DTO
     */
    @Override
    public AuthenticationDTO mapAuthenticationDTO(User user) {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setUsername(user.getUsername());
        authenticationDTO.setEmail(user.getEmail());
        authenticationDTO.setPhone(user.getPhone());
        authenticationDTO.setRole(user.getRole());
        return authenticationDTO;
    }
}
