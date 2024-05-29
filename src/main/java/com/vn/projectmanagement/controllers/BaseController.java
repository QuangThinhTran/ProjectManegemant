package com.vn.projectmanagement.controllers;

import com.vn.projectmanagement.entity.dto.ResponseAuthDTO;
import com.vn.projectmanagement.entity.dto.ResponseDTO;
import com.vn.projectmanagement.entity.dto.ResponseDataDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController
{
    /**
     * Response success response entity.
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<ResponseDTO> responseSuccess(String message) {
        ResponseDTO responseDTO = new ResponseDTO(message, HttpStatus.OK.value());
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * Response created response entity.
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<ResponseDTO> responseCreated(String message) {
        ResponseDTO responseDTO = new ResponseDTO(message, HttpStatus.CREATED.value());
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * Response with auth data response entity.
     * @param data - data to be returned
     * @param message - message to be returned
     * @param status - status to be returned
     * @param token - token to be returned
     * @return ResponseEntity<ResponseAuthDTO>
     */
    public ResponseEntity<ResponseAuthDTO> responseWithAuthData(Object data, String message, int status, String token) {
        ResponseAuthDTO responseAuthDTO = new ResponseAuthDTO(data, message, status, token);
        return ResponseEntity.status(status).body(responseAuthDTO);
    }

    /**
     * Response with data response entity.
     * @param data - data to be returned
     * @param message - message to be returned
     * @param status - status to be returned
     * @return ResponseEntity<ResponseDataDTO>
     */
    public ResponseEntity<ResponseDataDTO> responseWithData(Object data, String message, int status) {
        ResponseDataDTO responseDataDTO = new ResponseDataDTO(data, message, status);
        return ResponseEntity.status(status).body(responseDataDTO);
    }
}
