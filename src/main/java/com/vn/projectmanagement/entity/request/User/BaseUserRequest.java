package com.vn.projectmanagement.entity.request.User;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class BaseUserRequest {
    @NotBlank(message = "Username" + ValidationConstants.IS_REQUIRED)
    private String username;

    @NotBlank(message = "Email" + ValidationConstants.IS_REQUIRED)
    @Email(message = "Email" + ValidationConstants.IS_NOT_VALID_EMAIL)
    private String email;

    @NotBlank(message = "Phone" + ValidationConstants.IS_REQUIRED)
    @Length(min = 10, max = 11, message = "Phone" + ValidationConstants.LENGTH + "10 and 11")
    private String phone;
}
