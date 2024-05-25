package com.vn.projectmanagement.controllers;

import com.vn.projectmanagement.entity.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiController
{
    /**
     * Response success response entity.
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<ResponseDTO> responseSuccess(String message) {
        ResponseDTO responseDTO = new ResponseDTO(message, HttpStatus.OK);
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * Response created response entity.
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<ResponseDTO> responseCreated(String message) {
        ResponseDTO responseDTO = new ResponseDTO(message, HttpStatus.CREATED);
        return ResponseEntity.ok().body(responseDTO);
    }

    /**
     * Response error response entity.
     * @param message the message
     * @return ResponseEntity<ResponseDTO>
     */
    public ResponseEntity<ResponseDTO> responseError(String message) {
        ResponseDTO responseDTO = new ResponseDTO(message, HttpStatus.OK);
        return ResponseEntity.badRequest().body(responseDTO);
    }
}
