package com.vn.projectmanagement.entity.request;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequest
{
    @NotBlank(message = "Role name" + ValidationConstants.IS_REQUIRED)
    @Column(name = "name")
    private String name;
}