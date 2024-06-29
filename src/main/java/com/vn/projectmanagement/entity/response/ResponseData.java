package com.vn.projectmanagement.entity.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseData<T> extends Response {
    public T data;

    public ResponseData(T data, HttpStatus status) {
        super(status);
        this.data = data;
    }
}
