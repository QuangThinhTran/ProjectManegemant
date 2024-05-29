package com.vn.projectmanagement.entity.request;

import com.vn.projectmanagement.common.constants.ValidationConstants;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class LoginRequest {
    @NotBlank(message = "Username" + ValidationConstants.IS_REQUIRED)
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password" + ValidationConstants.IS_REQUIRED)
    @Length(min = 6, message = "Password" + ValidationConstants.LENGTH + 6)
    @Column(name = "password")
    private String password;
}
