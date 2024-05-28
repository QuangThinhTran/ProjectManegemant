package com.vn.projectmanagement.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {
    private String name;
    private String email;
    private String username;
    private String password;
}
