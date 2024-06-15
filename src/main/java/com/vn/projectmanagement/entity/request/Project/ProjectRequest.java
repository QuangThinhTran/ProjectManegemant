package com.vn.projectmanagement.entity.request.Project;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProjectRequest {
    @NotBlank(message = "Title " + ValidationConstants.IS_REQUIRED)
    private String title;

    @NotBlank(message = "Description " + ValidationConstants.IS_REQUIRED)
    private String description;
}
