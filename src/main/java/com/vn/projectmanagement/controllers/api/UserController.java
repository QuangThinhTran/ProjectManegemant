package com.vn.projectmanagement.controllers.api;

import com.vn.projectmanagement.common.constants.PathConstants;
import com.vn.projectmanagement.common.swagger.SwaggerHelper;
import com.vn.projectmanagement.common.swagger.SwaggerHttpStatus;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.common.utils.UploadFile;
import com.vn.projectmanagement.config.SwaggerConfig;
import com.vn.projectmanagement.controllers.BaseController;
import com.vn.projectmanagement.entity.dto.AuthenticationDTO;
import com.vn.projectmanagement.entity.dto.User.UserDTO;
import com.vn.projectmanagement.entity.response.Response;
import com.vn.projectmanagement.entity.response.ResponseData;
import com.vn.projectmanagement.entity.response.ResponseDataMessage;
import com.vn.projectmanagement.entity.request.User.UpdatePasswordRequest;
import com.vn.projectmanagement.entity.request.User.UpdateUserRequest;
import com.vn.projectmanagement.entity.response.ResponsePageable;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.mapped.interfaces.IUserMapped;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User Controller", description = "These endpoints are used to perform actions on user.")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
@RestController
@RequestMapping(PathConstants.API_USER)
public class UserController extends BaseController {

    private final UserService userService;
    private final AuthService authService;
    private final UploadFile uploadFile;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IUserMapped userMapped;

    @Autowired
    public UserController(
            UserService userService,
            AuthService authService,
            UploadFile uploadFile,
            IUserMapped userMapped
    ) {
        this.userService = userService;
        this.authService = authService;
        this.uploadFile = uploadFile;
        this.userMapped = userMapped;
    }

    /**
     * Find user by email
     *
     * @param email - Email of user
     * @return - Response entity with user data
     */
    @Operation(summary = SwaggerMessages.GET_USER_BY_EMAIL, tags = {"User Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/detail/{email}")
    public ResponseEntity<ResponseData<UserDTO>> findUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        UserDTO userDto = userMapped.mapUserDTO(user);
        return this.responseWithData(userDto, HttpStatus.OK);
    }

    /**
     * Get all users
     *
     * @param size     - Number of records per page
     * @param page     - Page number
     * @return - Response entity with list of users
     */
    @Operation(summary = SwaggerMessages.GET_ALL_USERS, tags = {"User Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/list")
    public ResponseEntity<ResponsePageable<UserDTO>> findAllUsers(
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            Page<User> usersPage = userService.filterUsers(page, size);
            Page<UserDTO> userDTOPage = userMapped.convertPageUserToPageUserDTO(usersPage);
            return this.responseWithPageable(userDTOPage.getContent(), userDTOPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update user
     *
     * @param username      - Username of user
     * @param userRequest   - Request body for updating user
     * @param bindingResult - Binding result for validation
     * @return - Response entity with user data
     */
    @Operation(summary = SwaggerMessages.UPDATE_USER)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPDATE_USER_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PutMapping("/update/{username}")
    public ResponseEntity<ResponseDataMessage<AuthenticationDTO>> update(
            @PathVariable("username") String username,
            @Valid @RequestBody UpdateUserRequest userRequest,
            BindingResult bindingResult
    ) {
        try {
            User user = this.userService.update(username, userRequest);
            AuthenticationDTO authenticationDTO = this.authService.mapAuthenticationDTO(user);
            return this.responseWithDataMessage(authenticationDTO, SwaggerMessages.UPDATE_USER, HttpStatus.OK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Change password of user
     *
     * @param username              - Username of user
     * @param updatePasswordRequest - Request body for updating password
     * @param bindingResult         - Binding result for validation
     * @return - Response entity with success message
     */
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
        try {
            this.userService.updatePassword(username, updatePasswordRequest);
            return this.responseSuccess(SwaggerMessages.CHANGE_PASSWORD);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Upload avatar of user
     *
     * @param file     - File to be uploaded
     * @param username - Username of user
     * @return - Response entity with success message
     */
    @Operation(summary = SwaggerMessages.UPLOAD_FILE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPLOAD_FILE_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON)),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping(value = "/upload-avatar/{username}", consumes = "multipart/form-data")
    public ResponseEntity<Response> uploadAvatar(
            @RequestPart("file") MultipartFile file,
            @PathVariable("username") String username
    ) {
        try {
            String pathFile = uploadFile.uploadFile(file);
            this.userService.uploadAvatar(username, pathFile);
            return this.responseSuccess(SwaggerMessages.UPLOAD_FILE);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
