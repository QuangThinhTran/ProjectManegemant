package com.vn.projectmanagement.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "These endpoints are used to perform actions on user.")
@RestController
@RequestMapping("/api/users")
public class UserController {

}
