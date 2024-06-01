package com.vn.projectmanagement.entity.request.Role;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequest
{
    @NotBlank(message = "Role name" + ValidationConstants.IS_REQUIRED)
    private String name;
}