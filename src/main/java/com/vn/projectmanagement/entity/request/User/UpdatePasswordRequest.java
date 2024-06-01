package com.vn.projectmanagement.entity.request.User;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UpdatePasswordRequest
{
    @NotBlank(message = "Password" + ValidationConstants.IS_REQUIRED)
    @NotNull(message = "Password" + ValidationConstants.IS_EMPTY)
    @Length(min = 6, message = "Password" + ValidationConstants.LENGTH + 6)
    private String oldPassword;

    @NotBlank(message = "Confirm password" + ValidationConstants.IS_REQUIRED)
    @NotNull(message = "Password" + ValidationConstants.IS_EMPTY)
    @Length(min = 6, message = "Confirm password" + ValidationConstants.LENGTH + 6)
    private String newPassword;
}
