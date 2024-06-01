package com.vn.projectmanagement.entity.request.Auth;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import com.vn.projectmanagement.entity.request.User.BaseUserRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterRequest extends BaseUserRequest {
    @NotBlank(message = "Password" + ValidationConstants.IS_REQUIRED)
    @Length(min = 6, message = "Password" + ValidationConstants.LENGTH + 6)
    private String password;

    @NotBlank(message = "Confirm password" + ValidationConstants.IS_REQUIRED)
    @Length(min = 6, message = "Confirm password" + ValidationConstants.LENGTH + 6)
    private String confirmPassword;

    @NotNull(message = "Role" + ValidationConstants.IS_REQUIRED)
    private UUID role;
}
