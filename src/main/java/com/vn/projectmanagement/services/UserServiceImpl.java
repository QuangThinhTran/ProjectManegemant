package com.vn.projectmanagement.services;

import com.vn.projectmanagement.common.constants.ExceptionConstant;
import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.entity.request.Auth.RegisterRequest;
import com.vn.projectmanagement.entity.request.User.UpdatePasswordRequest;
import com.vn.projectmanagement.entity.request.User.UpdateUserRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.repositories.UserRepository;
import com.vn.projectmanagement.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    /**
     * Find a specific record of User by email
     *
     * @param email - email
     * @return User
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException(ExceptionConstant.USER_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    /**
     * Create a newly record of User within provided register request and role
     *
     * @param registerRequest - register request
     * @param role            - role
     * @return user
     */
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
        if (userRepository.existsByEmail(email)) {
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
        if (userRepository.existsByPhone(phone)) {
            throw new ApiRequestException(phone + ValidationConstants.IS_EXISTED, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Page<User> filterUsers(Pageable pageable, int page, int size) {
        return this.userRepository.listUser(PageRequest.of(page, size));
    }

    /**
     * Update a specific user
     *
     * @param username          - username
     * @param updateUserRequest - update user request
     * @return User
     */
    @Override
    public User update(String username, UpdateUserRequest updateUserRequest) {
        User user = findByUsername(username);

        user.setUsername(updateUserRequest.getUsername());
        user.setEmail(updateUserRequest.getEmail());
        user.setPhone(updateUserRequest.getPhone());
        userRepository.save(user);
        return user;
    }

    /**
     * Update password of a specific user
     *
     * @param username              - username
     * @param updatePasswordRequest - update password request
     */
    @Override
    public void updatePassword(String username, UpdatePasswordRequest updatePasswordRequest) {
        User user = findByUsername(username);

        if (!this.passwordEncoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new ApiRequestException(ExceptionConstant.PASSWORD_NOT_MATCHED, HttpStatus.BAD_REQUEST);
        }

        String password = this.passwordEncoder.encode(updatePasswordRequest.getNewPassword());
        user.setPassword(password);
        userRepository.save(user);
    }
}
