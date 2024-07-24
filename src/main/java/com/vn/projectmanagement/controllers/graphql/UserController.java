package com.vn.projectmanagement.controllers.graphql;

import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.services.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.*;

@RestController(value = "UserControllerGraphQL")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Autowired
    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @QueryMapping
    public Page<User> findAllUsers(@Argument int page, @Argument int size) {
        try {
            return userService.filterUsers(page, size);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @QueryMapping
    public User findUserByEmail(@Argument String email) {
        try {
            return userService.findByEmail(email);
        } catch (ApiRequestException e) {
            logger.error(e.getMessage());
            return null;
        }
    }
}
