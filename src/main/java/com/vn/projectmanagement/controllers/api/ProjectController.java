package com.vn.projectmanagement.controllers.api;

import com.vn.projectmanagement.common.constants.PathConstants;
import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.common.swagger.SwaggerHelper;
import com.vn.projectmanagement.common.swagger.SwaggerHttpStatus;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.config.SwaggerConfig;
import com.vn.projectmanagement.controllers.BaseController;
import com.vn.projectmanagement.entity.dto.Project.ProjectDTO;
import com.vn.projectmanagement.entity.request.Project.InviteStaffRequest;
import com.vn.projectmanagement.entity.request.Project.ProjectRequest;
import com.vn.projectmanagement.entity.response.Response;
import com.vn.projectmanagement.entity.response.ResponseData;
import com.vn.projectmanagement.entity.response.ResponsePageable;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.mapped.interfaces.IProjectMapped;
import com.vn.projectmanagement.models.Project;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.services.interfaces.ProjectService;
import com.vn.projectmanagement.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Project Controller", description = "These endpoints are used to perform actions on Project")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
@RestController(value = "ProjectControllerAPI")
@RequestMapping(PathConstants.API_PROJECT)
public class ProjectController extends BaseController {

    private final ProjectService projectService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final IProjectMapped projectMapped;

    @Autowired
    public ProjectController(ProjectService projectService, UserService userService, IProjectMapped projectMapped) {
        this.projectService = projectService;
        this.userService = userService;
        this.projectMapped = projectMapped;
    }

    /**
     * List projects response entity.
     *
     * @param page - page number
     * @param size - size of page
     * @return ResponseEntity<ResponsePageable < Project>>
     */
    @Operation(summary = SwaggerMessages.GET_ALL_PROJECTS, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/list")
    public ResponseEntity<ResponsePageable<ProjectDTO>> listProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<Project> projects = projectService.listProjects(page, size);
            Page<ProjectDTO> projectDTOsPage = projectMapped.convertPageProjectToPageProjectDTO(projects);
            return responseWithPageable(projectDTOsPage.getContent(), projectDTOsPage);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create project response entity.
     *
     * @param projectRequest - project request
     * @param bindingResult  - binding result
     * @return ResponseEntity<Response>
     */
    @Operation(summary = SwaggerMessages.CREATE_PROJECT, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.CREATED, description = SwaggerMessages.CREATE_PROJECT_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/create")
    public ResponseEntity<Response> createProject(
            @RequestBody ProjectRequest projectRequest,
            BindingResult bindingResult
    ) {
        try {
            projectService.createProject(projectRequest);
            return responseCreated(SwaggerMessages.CREATE_PROJECT);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get project by title response entity.
     *
     * @param title - title
     * @return ResponseEntity<ResponseData < Project>>
     */
    @Operation(summary = SwaggerMessages.GET_PROJECT_BY_TITLE, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/detail/{title}")
    public ResponseEntity<ResponseData<ProjectDTO>> getProjectByTitle(
            @PathVariable String title
    ) {
        try {
            Project project = projectService.getProjectByTitle(title);
            ProjectDTO projectDTO = projectMapped.mapProjectDTO(project);
            return responseWithData(projectDTO, HttpStatus.OK);
        } catch (ApiRequestException e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        }
    }

    /**
     * Update project response entity.
     *
     * @param id             - id
     * @param projectRequest - project request
     * @param bindingResult  - binding result
     * @return ResponseEntity<Response>
     */
    @Operation(summary = SwaggerMessages.UPDATE_PROJECT, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPDATE_PROJECT_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Response> updateProject(
            @PathVariable UUID id,
            @RequestBody ProjectRequest projectRequest,
            BindingResult bindingResult
    ) {
        try {
            projectService.updateProject(id, projectRequest);
            return responseSuccess(SwaggerMessages.UPDATE_PROJECT);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete project response entity.
     *
     * @param id - id
     * @return ResponseEntity<Response>
     */
    @Operation(summary = SwaggerMessages.DELETE_PROJECT, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.DELETE_PROJECT_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Response> deleteProject(
            @PathVariable UUID id
    ) {
        try {
            projectService.deleteProject(projectService.getProjectById(id));
            return responseSuccess(SwaggerMessages.DELETE_PROJECT);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Invite staff to project response entity.
     *
     * @param email              - email
     * @param inviteStaffRequest - inviteStaffRequest
     * @param bindingResult      - bindingResult
     * @return ResponseEntity<Response>
     */
    @Operation(summary = SwaggerMessages.INVITE_STAFF, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.INVITE_STAFF_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/invite-staff/{email}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Response> inviteStaffToProject(
            @PathVariable String email,
            @RequestBody InviteStaffRequest inviteStaffRequest,
            BindingResult bindingResult
    ) {
        try {
            User user = userService.findByEmail(email);
            Project project = projectService.getProjectById(inviteStaffRequest.getProjectID());

            if (!projectService.inviteStaffToProject(user, project)) {
                return responseSuccess(user.getEmail() + ValidationConstants.IS_EXISTED);
            }
            projectService.sendMailInvitedStaff(email, project);

            return responseSuccess(SwaggerMessages.INVITE_STAFF);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Remove staff from project response entity.
     *
     * @param email              - email
     * @param inviteStaffRequest - inviteStaffRequest
     * @param bindingResult      - bindingResult
     * @return ResponseEntity<Response>
     */
    @Operation(summary = SwaggerMessages.REMOVE_STAFF, tags = {"Project Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.REMOVE_STAFF_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Project.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/remove-staff/{email}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Response> removeStaffFromProject(
            @PathVariable String email,
            @RequestBody InviteStaffRequest inviteStaffRequest,
            BindingResult bindingResult
    ) {
        try {
            User user = userService.findByEmail(email);
            Project project = projectService.getProjectById(inviteStaffRequest.getProjectID());

            if (!projectService.removeStaffFromProject(user, project)) {
                return responseSuccess(user.getEmail() + ValidationConstants.IS_NOT_FOUND);
            }
            return responseSuccess(SwaggerMessages.REMOVE_STAFF);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
