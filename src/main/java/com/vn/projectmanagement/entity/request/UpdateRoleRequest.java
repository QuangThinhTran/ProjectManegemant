package com.vn.projectmanagement.entity.request;

import com.vn.projectmanagement.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRoleRequest
{
    @NotBlank(message = "Old name" + ValidationMessages.IS_REQUIRED)
    public String oldName;

    @NotBlank(message = "New name" + ValidationMessages.IS_REQUIRED)
    public String newName;
}
