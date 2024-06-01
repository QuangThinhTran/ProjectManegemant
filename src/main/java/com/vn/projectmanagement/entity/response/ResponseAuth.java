package com.vn.projectmanagement.entity.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseAuth extends Response {
    public Object data;
    public final String token;

    public ResponseAuth(Object data, String message, HttpStatus status, String token) {
        super(message, status);
        this.data = data;
        this.token = token;
    }
}
