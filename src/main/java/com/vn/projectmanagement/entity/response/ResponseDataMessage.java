package com.vn.projectmanagement.entity.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseDataMessage<T> extends Response {
    public T data;

    public ResponseDataMessage(T data, String message, HttpStatus status) {
        super(message, status);
        this.data = data;
    }
}
