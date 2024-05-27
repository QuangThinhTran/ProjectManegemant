package com.vn.projectmanagement.entity.request;

import com.vn.projectmanagement.common.messages.ValidationMessages;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequest
{
    @NotBlank(message = "Role name" + ValidationMessages.IS_REQUIRED)
    private String name;
}