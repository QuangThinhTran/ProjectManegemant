package com.vn.projectmanagement.entity.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseDTO {
    public String message;
    public HttpStatus status;

    public ResponseDTO(String message, HttpStatus status) {
        this.status = status;
        this.message = message;
    }
}
