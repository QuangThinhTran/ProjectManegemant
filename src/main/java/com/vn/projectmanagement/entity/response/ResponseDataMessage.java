package com.vn.projectmanagement.entity.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDataMessage extends Response {
    public Object data;

    public ResponseDataMessage(Object data, String message, HttpStatus status) {
        super(message, status);
        this.data = data;
    }
}
