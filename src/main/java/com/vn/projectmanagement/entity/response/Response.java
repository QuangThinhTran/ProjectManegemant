package com.vn.projectmanagement.entity.response;

import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class Response {
    private String message = SwaggerMessages.OK;
    private final int status;

    public Response(HttpStatus status) {
        this.status = status.value();
    }

    public Response(String message, HttpStatus status) {
        this.status = status.value();
        this.message = message;
    }
}
