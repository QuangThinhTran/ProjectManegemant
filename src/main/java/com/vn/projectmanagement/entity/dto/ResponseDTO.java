package com.vn.projectmanagement.entity.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDTO {
    public String message;
    public int status;

    public ResponseDTO(String message, int status) {
        this.status = status;
        this.message = message;
    }
}
