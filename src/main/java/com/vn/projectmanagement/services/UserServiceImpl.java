package com.vn.projectmanagement.services;

import com.vn.projectmanagement.common.constants.ExceptionConstant;
import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.entity.request.RegisterRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.UserRepository;
import com.vn.projectmanagement.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Find a specific record of User by username
     *
     * @param username - username
     * @return User
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiRequestException(ExceptionConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public User create(RegisterRequest registerRequest, Role role) {
        String password = this.passwordEncoder.encode(registerRequest.getPassword());

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(password);
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setRole(role);
        userRepository.save(user);
        return user;
    }

    /**
     * Check if the username is existed
     *
     * @param username - username
     */
    public void checkUsernameExist(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new ApiRequestException(username + ValidationConstants.IS_EXISTED, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Check if the email is existed
     *
     * @param email - email
     */
    @Override
    public void checkEmailExist(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            throw new ApiRequestException(email + ValidationConstants.IS_EXISTED, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Check if the phone is existed
     *
     * @param phone - phone
     */
    @Override
    public void checkPhoneExist(String phone) {
        User user = userRepository.findByPhone(phone);
        if (user != null) {
            throw new ApiRequestException(phone + ValidationConstants.IS_EXISTED, HttpStatus.BAD_REQUEST);
        }
    }
}
