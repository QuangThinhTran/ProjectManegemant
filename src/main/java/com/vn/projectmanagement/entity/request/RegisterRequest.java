package com.vn.projectmanagement.entity.request;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Data
public class RegisterRequest {
    @NotBlank(message = "Username" + ValidationConstants.IS_REQUIRED)
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password" + ValidationConstants.IS_REQUIRED)
    @Length(min = 6, message = "Password" + ValidationConstants.LENGTH + 6)
    @Column(name = "password")
    private String password;

    @NotBlank(message = "Confirm password" + ValidationConstants.IS_REQUIRED)
    @Length(min = 6, message = "Confirm password" + ValidationConstants.LENGTH + 6)
    private String confirmPassword;

    @NotBlank(message = "Email" + ValidationConstants.IS_REQUIRED)
    @Email(message = "Email" + ValidationConstants.IS_NOT_VALID_EMAIL)
    @Column(name = "email")
    private String email;

    @NotBlank(message = "Phone" + ValidationConstants.IS_REQUIRED)
    @Length(min = 10, max = 11, message = "Phone" + ValidationConstants.LENGTH + "10 and 11")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "Role" + ValidationConstants.IS_REQUIRED)
    @Column(name = "role_id")
    private UUID role;
}
