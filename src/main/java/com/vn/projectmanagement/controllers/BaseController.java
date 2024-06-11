package com.vn.projectmanagement.controllers;

import com.vn.projectmanagement.entity.response.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController {
    /**
     * Response success response entity.
     *
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<Response> responseSuccess(String message) {
        Response response = new Response(message, HttpStatus.OK);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Response created response entity.
     *
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<Response> responseCreated(String message) {
        Response response = new Response(message, HttpStatus.CREATED);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    /**
     * Response with auth data response entity.
     *
     * @param data    - data to be returned
     * @param message - message to be returned
     * @param status  - status to be returned
     * @param token   - token to be returned
     * @return ResponseEntity<ResponseAuthDTO>
     */
    public <T> ResponseEntity<ResponseAuth<T>> responseWithAuthData(
            T data,
            String message,
            HttpStatus status,
            String token
    ) {
        ResponseAuth<T> responseAuthDTO = new ResponseAuth<>(data, message, status, token);
        return ResponseEntity.status(status).body(responseAuthDTO);
    }

    /**
     * Response with data response entity.
     *
     * @param data    - data to be returned
     * @param message - message to be returned
     * @param status  - status to be returned
     * @return ResponseEntity<ResponseDataDTO>
     */
    public <T> ResponseEntity<ResponseDataMessage<T>> responseWithDataMessage(T data, String message, HttpStatus status) {
        ResponseDataMessage<T> responseDataMessageDTO = new ResponseDataMessage<>(data, message, status);
        return ResponseEntity.status(status).body(responseDataMessageDTO);
    }

    /**
     * Response with data response entity.
     *
     * @param data   - data to be returned
     * @param status - status to be returned
     * @return ResponseEntity<ResponseData>
     */
    public <T> ResponseEntity<ResponseData<T>> responseWithData(T data, HttpStatus status) {
        ResponseData<T> responseDataDTO = new ResponseData<>(data, status);
        return ResponseEntity.status(status).body(responseDataDTO);
    }

    /**
     * Response with data response entity.
     *
     * @param data     - data to be returned
     * @param pageable - pageable to be returned
     * @param <T>      - <T> to be returned
     * @return ResponsePageable
     */
    public <T> ResponseEntity<ResponsePageable<T>> responseWithPageable(List<T> data, Page<T> pageable) {
        ResponsePageable<T> responsePageable = new ResponsePageable<>(data, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responsePageable);
    }
}
