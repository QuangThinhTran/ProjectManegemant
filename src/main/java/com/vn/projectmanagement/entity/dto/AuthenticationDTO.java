package com.vn.projectmanagement.entity.dto;

import lombok.Data;

@Data
public class AuthenticationDTO {
    private String username;
    private String email;
    private String phone;
    private String role;
}
