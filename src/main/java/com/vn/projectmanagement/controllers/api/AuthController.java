package com.vn.projectmanagement.controllers.api;

import com.vn.projectmanagement.common.constants.PathConstants;
import com.vn.projectmanagement.common.swagger.SwaggerHelper;
import com.vn.projectmanagement.common.swagger.SwaggerHttpStatus;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.config.SwaggerConfig;
import com.vn.projectmanagement.controllers.BaseController;
import com.vn.projectmanagement.entity.dto.AuthenticationDTO;
import com.vn.projectmanagement.entity.response.ResponseAuth;
import com.vn.projectmanagement.entity.response.Response;
import com.vn.projectmanagement.entity.request.Auth.LoginRequest;
import com.vn.projectmanagement.entity.request.Auth.RegisterRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.services.interfaces.AuthService;
import com.vn.projectmanagement.services.interfaces.RoleService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth Controller", description = "These endpoints are used to perform actions on authentication.")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
@RestController
@Validated
@RequestMapping(PathConstants.API_AUTH)
public class AuthController extends BaseController {

    private final RoleService roleService;
    private final AuthService authService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(RoleService roleService, AuthService authService, UserService userService) {
        this.roleService = roleService;
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Register a new user
     *
     * @param registerRequest - Request body for registration
     * @param bindingResult   - Binding result for validation
     * @return - Response entity with user data and token
     */
    @Operation(summary = SwaggerMessages.REGISTRATION_SUCCESS_MESSAGE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.CREATED, description = SwaggerMessages.REGISTRATION_SUCCESS_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = ResponseAuth.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseAuth<AuthenticationDTO>> register(
            @Valid @RequestBody RegisterRequest registerRequest,
            BindingResult bindingResult
    ) {
        try {
            this.userService.checkUsernameExist(registerRequest.getUsername());

            this.userService.checkEmailExist(registerRequest.getEmail());

            this.userService.checkPhoneExist(registerRequest.getPhone());

            this.authService.checkPasswordMatched(registerRequest.getPassword(), registerRequest.getConfirmPassword());

            Role role = this.roleService.findById(registerRequest.getRole());
            AuthenticationDTO user = authService.createUser(registerRequest, role);
            String token = this.authService.generateToken(user);

            return this.responseWithAuthData(
                    user,
                    SwaggerMessages.REGISTRATION_SUCCESS_MESSAGE,
                    HttpStatus.CREATED,
                    token
            );
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Login a user
     *
     * @param loginRequest - Request body for login
     * @return - Response entity with user data and token
     */
    @Operation(summary = SwaggerMessages.LOGIN_SUCCESS_MESSAGE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.LOGIN_SUCCESS_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = ResponseAuth.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseAuth<AuthenticationDTO>> login(
            @Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult
    ) {
        try {
            User user = this.userService.findByUsername(loginRequest.getUsername());
            this.authService.login(loginRequest);
            AuthenticationDTO authenticationDTO = this.authService.mapAuthenticationDTO(user);
            String token = this.authService.generateToken(authenticationDTO);
            return responseWithAuthData(authenticationDTO, SwaggerMessages.LOGIN_SUCCESS_MESSAGE, HttpStatus.OK, token);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Logout a user
     *
     * @return - Response entity with success message
     */
    @Operation(summary = SwaggerMessages.LOGOUT_SUCCESS_MESSAGE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.LOGOUT_SUCCESS_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Response.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/logout")
    public ResponseEntity<Response> logout() {
        try {
            this.authService.logout();
            return this.responseSuccess(SwaggerMessages.LOGOUT_SUCCESS_MESSAGE);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(
                    SwaggerMessages.INTERNAL_SERVER_ERROR,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
