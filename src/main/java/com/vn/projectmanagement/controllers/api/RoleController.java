package com.vn.projectmanagement.controllers.api;

import com.vn.projectmanagement.common.constants.PathConstants;
import com.vn.projectmanagement.common.swagger.SwaggerHelper;
import com.vn.projectmanagement.common.swagger.SwaggerHttpStatus;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.config.SwaggerConfig;
import com.vn.projectmanagement.controllers.BaseController;
import com.vn.projectmanagement.entity.response.Response;
import com.vn.projectmanagement.entity.request.Role.CreateRoleRequest;
import com.vn.projectmanagement.entity.request.Role.UpdateRoleRequest;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.models.Role;
import com.vn.projectmanagement.repositories.RoleRepository;
import com.vn.projectmanagement.services.interfaces.RoleService;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Role Controller", description = "These endpoints are used to perform actions on role.")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
// Yêu cầu xác thực khi truy cập các endpoint trong controller này (đã được cấu hình trong SwaggerConfig)
@RestController
@Validated
@RequestMapping(PathConstants.API_ROLE)
public class RoleController extends BaseController {

    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    public RoleController(
            RoleRepository roleRepository,
            RoleService roleService
    ) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    /**
     * Get all roles
     *
     * @return - Response entity with list of roles
     */
    @Operation(summary = SwaggerMessages.GET_ALL_ROLES)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.GET_ALL_ROLES, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Role.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/all")
    public ResponseEntity<List<Role>> getRoles() {
        try {
            return ResponseEntity.ok().body(roleRepository.findAll());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create a new role
     *
     * @param role          - Request body for creating a new role
     * @param bindingResult - Binding result for validation
     * @return - Response entity with success message
     */
    @Operation(summary = SwaggerMessages.CREATE_ROLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.CREATED, description = SwaggerMessages.CREATE_ROLE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.CREATE_ROLE_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/create")
    public ResponseEntity<Response> create(@Valid @RequestBody CreateRoleRequest role, BindingResult bindingResult) {
        try {
            roleService.checkRoleExist(role.getName());
            roleService.createRole(role.getName());
            return this.responseCreated(SwaggerMessages.CREATE_ROLE);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = SwaggerMessages.UPDATE_ROLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPDATE_ROLE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UPDATE_ROLE_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PutMapping("/update")
    public ResponseEntity<Response> update(@Valid @RequestBody UpdateRoleRequest role, BindingResult bindingResult) {
        try {
            roleService.checkRoleExist(role.getNewName());
            roleService.updateRole(role.getOldName(), role.getNewName());
            return this.responseSuccess(SwaggerMessages.UPDATE_ROLE);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete a role by name
     *
     * @param roleName - Role name to delete
     * @return - Response entity with success message
     */
    @Operation(summary = SwaggerMessages.DELETE_ROLE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.DELETE_ROLE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.DELETE_ROLE_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @DeleteMapping("/delete/{roleName}")
    public ResponseEntity<Response> delete(@PathVariable String roleName) {
        try {
            roleService.deleteByName(roleName);
            return this.responseSuccess(SwaggerMessages.DELETE_ROLE);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
