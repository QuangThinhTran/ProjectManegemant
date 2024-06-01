package com.vn.projectmanagement.entity.request.Auth;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @NotBlank(message = "Username" + ValidationConstants.IS_REQUIRED)
    private String username;

    @NotBlank(message = "Password" + ValidationConstants.IS_REQUIRED)
    @Length(min = 6, message = "Password" + ValidationConstants.LENGTH + 6)
    private String password;
}
