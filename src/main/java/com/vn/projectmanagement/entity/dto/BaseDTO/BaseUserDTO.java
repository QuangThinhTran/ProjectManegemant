package com.vn.projectmanagement.entity.dto.BaseDTO;

import lombok.Data;

@Data
public class BaseUserDTO {
    private String username;
    private String email;
    private String phone;
    private String role;
    private String avatar;
}
