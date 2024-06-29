package com.vn.projectmanagement.entity.request.Task;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignTaskRequest {
    @NotBlank(message = "Task code " + ValidationConstants.IS_REQUIRED)
    private String taskCode;
}
