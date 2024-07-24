package com.vn.projectmanagement.controllers.api;

import com.vn.projectmanagement.common.constants.PathConstants;
import com.vn.projectmanagement.common.swagger.SwaggerHelper;
import com.vn.projectmanagement.common.swagger.SwaggerHttpStatus;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.config.SwaggerConfig;
import com.vn.projectmanagement.controllers.BaseController;
import com.vn.projectmanagement.entity.dto.Task.TaskDTO;
import com.vn.projectmanagement.entity.request.Task.AssignTaskRequest;
import com.vn.projectmanagement.entity.request.Task.TaskRequest;
import com.vn.projectmanagement.entity.request.Task.UpdateStatusTaskRequest;
import com.vn.projectmanagement.entity.response.Response;
import com.vn.projectmanagement.entity.response.ResponseData;
import com.vn.projectmanagement.entity.response.ResponsePageable;
import com.vn.projectmanagement.exceptions.ApiRequestException;
import com.vn.projectmanagement.mapped.interfaces.ITaskMapped;
import com.vn.projectmanagement.models.Task;
import com.vn.projectmanagement.models.User;
import com.vn.projectmanagement.services.interfaces.TaskService;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Task Controller", description = "These endpoints are used to perform actions on Task")
@SecurityRequirement(name = SwaggerConfig.SECURITY_SCHEME_NAME)
@RestController(value = "TaskControllerAPI")
@RequestMapping(PathConstants.API_TASK)
public class TaskController extends BaseController {

    private final TaskService taskService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final ITaskMapped taskMapped;

    @Autowired
    public TaskController(
            TaskService taskService,
            UserService userService,
            ITaskMapped taskMapped
    ) {
        this.taskService = taskService;
        this.userService = userService;
        this.taskMapped = taskMapped;
    }

    /**
     * Get all tasks
     *
     * @param status task status
     * @param page   page
     * @param size   size
     * @return response
     */
    @Operation(summary = SwaggerMessages.GET_ALL_TASKS, tags = {"Task Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @GetMapping("/list-by-status/{status}")
    public ResponseEntity<ResponsePageable<TaskDTO>> getFilterStatus(
            @PathVariable String status,
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size
    ) {
        try {
            Page<Task> tasks = taskService.listTasks(status, page, size);
            Page<TaskDTO> taskDTOS = taskMapped.convertPageTaskToPageTaskDTO(tasks);
            return responseWithPageable(taskDTOS.getContent(), taskDTOS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Create task
     *
     * @param taskRequest   task request
     * @param bindingResult binding result
     * @return response
     */
    @Operation(summary = SwaggerMessages.CREATE_TASK, tags = {"Task Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.CREATED, description = SwaggerMessages.CREATE_TASK_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PostMapping("/create")
    public ResponseEntity<Response> createTask(
            @RequestBody TaskRequest taskRequest,
            BindingResult bindingResult
    ) {
        try {
            taskService.createTask(taskRequest);
            return responseCreated(SwaggerMessages.CREATE_TASK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update task
     *
     * @param taskCode      task code
     * @param taskRequest   task request
     * @param bindingResult binding result
     * @return response
     */
    @Operation(summary = SwaggerMessages.UPDATE_TASK, tags = {"Task Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPDATE_TASK_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PutMapping("/update/{taskCode}")
    public ResponseEntity<Response> updateTask(
            @PathVariable String taskCode,
            @RequestBody TaskRequest taskRequest,
            BindingResult bindingResult
    ) {
        try {
            taskService.updateTask(taskCode, taskRequest);
            return responseSuccess(SwaggerMessages.UPDATE_TASK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Update status task
     *
     * @param taskCode                task code
     * @param updateStatusTaskRequest update status task request
     * @param bindingResult           binding result
     * @return response
     */
    @Operation(summary = SwaggerMessages.UPDATE_STATUS_TASK, tags = {"Task Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.UPDATE_STATUS_TASK_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PatchMapping("/update-status/{taskCode}")
    public ResponseEntity<Response> updateStatusTask(
            @PathVariable String taskCode,
            @RequestBody UpdateStatusTaskRequest updateStatusTaskRequest,
            BindingResult bindingResult
    ) {
        try {
            Task task = taskService.findTaskByCode(taskCode);

            taskService.updateStatus(task, updateStatusTaskRequest.getStatus());

            taskService.sendMailUpdateStatus(task.getUser().getEmail(), task.getProject(), task);

            return responseSuccess(SwaggerMessages.UPDATE_STATUS_TASK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Assign task to user
     *
     * @param email             email
     * @param assignTaskRequest assign task request
     * @param bindingResult     binding result
     * @return response
     */
    @Operation(summary = SwaggerMessages.ASSIGN_TASK, tags = {"Task Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.ASSIGN_TASK_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @PatchMapping("/assign-task/{email}")
    public ResponseEntity<Response> assignTaskToUser(
            @PathVariable String email,
            @RequestBody AssignTaskRequest assignTaskRequest,
            BindingResult bindingResult
    ) {
        try {
            User user = userService.findByEmail(email);
            Task task = taskService.findTaskByCode(assignTaskRequest.getTaskCode());

            taskService.assignTaskToUser(task, user);
            return responseSuccess(SwaggerMessages.ASSIGN_TASK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/detail/{taskCode}")
    public ResponseEntity<ResponseData<TaskDTO>> getTaskByCode(@PathVariable String taskCode) {
        try {
            Task task = taskService.findTaskByCode(taskCode);
            TaskDTO taskDTO = taskMapped.mapTaskDTO(task);
            return responseWithData(taskDTO, HttpStatus.OK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete task
     *
     * @param taskCode task code
     * @return response
     */
    @Operation(summary = SwaggerMessages.DELETE_TASK, tags = {"Task Controller"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = SwaggerHttpStatus.OK, description = SwaggerMessages.DELETE_TASK_MESSAGE, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = SwaggerHttpStatus.BAD_REQUEST, description = SwaggerMessages.BAD_REQUEST, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.BAD_REQUEST_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.UNAUTHORIZED, description = SwaggerMessages.UNAUTHORIZED, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.UNAUTHORIZED_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.FORBIDDEN, description = SwaggerMessages.FORBIDDEN, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.FORBIDDEN_MESSAGE))),
            @ApiResponse(responseCode = SwaggerHttpStatus.INTERNAL_SERVER_ERROR, description = SwaggerMessages.INTERNAL_SERVER_ERROR, content = @Content(mediaType = SwaggerHelper.APPLICATION_JSON, schema = @Schema(example = SwaggerMessages.INTERNAL_SERVER_ERROR_MESSAGE)))
    })
    @DeleteMapping("/delete/{taskCode}")
    public ResponseEntity<Response> deleteTask(@PathVariable String taskCode) {
        try {
            Task task = taskService.findTaskByCode(taskCode);

            taskService.deleteTask(task);

            return responseSuccess(SwaggerMessages.DELETE_TASK);
        } catch (ApiRequestException e) {
            throw new ApiRequestException(e.getMessage(), e.getStatus());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApiRequestException(SwaggerMessages.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
