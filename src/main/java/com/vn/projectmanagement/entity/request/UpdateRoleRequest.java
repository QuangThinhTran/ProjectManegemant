package com.vn.projectmanagement.entity.request;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRoleRequest
{
    @NotBlank(message = "Old name" + ValidationConstants.IS_REQUIRED)
    public String oldName;

    @NotBlank(message = "New name" + ValidationConstants.IS_REQUIRED)
    public String newName;
}
