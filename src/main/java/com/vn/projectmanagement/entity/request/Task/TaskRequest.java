package com.vn.projectmanagement.entity.request.Task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class TaskRequest {
    private String taskCode;

    @NotBlank(message = "Title " + ValidationConstants.IS_REQUIRED)
    private String title;

    @NotBlank(message = "Description " + ValidationConstants.IS_REQUIRED)
    private String description;

    @NotBlank(message = "Status " + ValidationConstants.IS_REQUIRED)
    private String status;

    @NotBlank(message = "Expiration Date" + ValidationConstants.IS_REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expirationDate;

    @NotBlank(message = "Project ID " + ValidationConstants.IS_REQUIRED)
    private UUID projectID;

    @NotBlank(message = "Email user " + ValidationConstants.IS_REQUIRED)
    private String userEmail;
}
