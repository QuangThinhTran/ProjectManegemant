package com.vn.projectmanagement.controllers.api;

import com.vn.projectmanagement.common.constants.PathConstants;
import com.vn.projectmanagement.common.swagger.SwaggerHelper;
import com.vn.projectmanagement.common.swagger.SwaggerHttpStatus;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.config.SwaggerConfig;
import com.vn.projectmanagement.controllers.BaseController;
import com.vn.projectmanagement.entity.dto.AuthenticationDTO;
import com.vn.projectmanagement.entity.response.Response;
import com.vn.projectmanagement.entity.response.ResponseData;
import com.vn.projectmanagement.entity.response.ResponseDataMessage;
import com.vn.projectmanagement.entity.request.User.UpdatePasswordRequest;
import com.vn.projectmanagement.entity.request.User.UpdateUserRequest;
import com.vn.projectmanagement.entity.response.ResponsePageable;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.services.interfaces.AuthService;
import com.vn.projectmanagement.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "These endpoints are used to perform actions on user.")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
@RestController
@RequestMapping(PathConstants.API_USER)
public class UserController extends BaseController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @Operation(summary = "Find user by email", description = "Find a specific record of User by email", tags = { "User Controller" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/detail/{email}")
    public ResponseEntity<ResponseData> findUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return this.responseWithData(user, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<ResponsePageable> findAllUsers(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page,
            Pageable pageable
    ) {
        Page<User> usersPage = userService.filterUsers(pageable, page, size);
        List<User> userList = usersPage.getContent();
        return this.responseWithPageable(userList, usersPage);
    }

    @Operation(summary = SwaggerMessages.UPDATE_USER)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPDATE_USER_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PutMapping("/update/{username}")
    public ResponseEntity<ResponseDataMessage> update(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdateUserRequest userRequest,
            BindingResult bindingResult
    ) {
        User user = this.userService.update(username, userRequest);
        AuthenticationDTO authenticationDTO = this.authService.mapAuthenticationDTO(user);
        return this.responseWithDataMessage(authenticationDTO, SwaggerMessages.UPDATE_ROLE, HttpStatus.OK);
    }

    @Operation(summary = SwaggerMessages.CHANGE_PASSWORD)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.CHANGE_PASSWORD_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON)),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PutMapping("/update-password/{username}")
    public ResponseEntity<Response> updatePassword(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
            BindingResult bindingResult
    ) {
        this.userService.updatePassword(username, updatePasswordRequest);
        return this.responseSuccess(SwaggerMessages.CHANGE_PASSWORD);
    }
}
