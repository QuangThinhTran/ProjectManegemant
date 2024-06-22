package com.vn.projectmanagement.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.vn.projectmanagement.entity.view.View;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDataMessage<T> extends Response {
    @JsonView(View.ProjectView.class)
    public T data;

    public ResponseDataMessage(T data, String message, HttpStatus status) {
        super(message, status);
        this.data = data;
    }
}
