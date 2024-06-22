package com.vn.projectmanagement.entity.response;

import com.fasterxml.jackson.annotation.JsonView;
import com.vn.projectmanagement.entity.view.View;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonView({View.UserView.class, View.ProjectView.class})
public class ResponseData<T> extends Response {
    public T data;

    public ResponseData(T data, HttpStatus status) {
        super(status);
        this.data = data;
    }
}
