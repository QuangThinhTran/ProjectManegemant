package com.vn.projectmanagement.entity.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseData extends Response {
    public Object data;

    public ResponseData(Object data, HttpStatus status) {
        super(status);
        this.data = data;
    }
}
