package com.vn.projectmanagement.entity.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDataDTO extends ResponseDTO {
    public Object data;

    public ResponseDataDTO(Object data, String message, int status) {
        super(message, status);
        this.data = data;
    }
}
