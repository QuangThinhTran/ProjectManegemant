package com.vn.projectmanagement.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import com.vn.projectmanagement.entity.view.View;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonView({View.UserView.class, View.ProjectView.class})
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
