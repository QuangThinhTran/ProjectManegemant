package com.vn.projectmanagement.entity.dto;

import lombok.Getter;

@Getter
public class ResponseAuthDTO extends ResponseDTO {
    public Object data;
    public final String token;

    public ResponseAuthDTO(Object data, String message, int status, String token) {
        super(message, status);
        this.data = data;
        this.token = token;
    }
}
