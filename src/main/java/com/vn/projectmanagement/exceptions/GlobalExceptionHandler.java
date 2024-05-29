package com.vn.projectmanagement.exceptions;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
@Validated
public class GlobalExceptionHandler {
    private static final String NOT_FOUND_URL = "Not found URL : ";

    /**
     * Xử lý các lỗi của các trường trong request
     *
     * @param ex ConstraintViolationException - đối tượng chứa thông tin lỗi của các trường
     * @return ResponseEntity<InputFieldException> - trả về thông báo lỗi và status code
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<InputFieldException> handleConstraintViolationException(ConstraintViolationException ex) {
        // Lưu thông tin lỗi của các trường vào một map
        Map<String, String> errors = new HashMap<>();
        // Lấy ra các lỗi của các trường
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();

        // Duyệt qua các lỗi của các trường và lưu vào map
        // Key: tên trường, Value: thông báo lỗi
        for (ConstraintViolation<?> violation : violations) {
            String fieldName = getLastFieldFromPropertyPath(violation.getPropertyPath().toString());
            String message = violation.getMessage();
            errors.put(fieldName, message);
        }

        InputFieldException inputFieldException = new InputFieldException(errors, HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(inputFieldException, HttpStatus.BAD_REQUEST);
    }

    /**
     * Xử lý các lỗi của API
     *
     * @param exception - đối tượng chứa thông tin lỗi và status code
     * @return ResponseEntity<ApiErrorResponse> - trả về thông báo lỗi và status code
     */
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleApiRequestException(ApiRequestException exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(exception.getMessage(), exception.getStatus().value());
        return ResponseEntity.status(exception.getStatus()).body(apiErrorResponse);
    }

    /**
     * Xử lý lỗi khi không tìm thấy URL
     *
     * @param exception - đối tượng chứa thông tin lỗi và status code
     * @return ResponseEntity<ApiErrorResponse> - trả về thông báo lỗi và status code
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(NoHandlerFoundException exception) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(NOT_FOUND_URL + exception.getRequestURL(), HttpStatus.NOT_FOUND.value());
        return new ResponseEntity<>(apiErrorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Lấy ra tên trường lỗi cuối cùng trong đường dẫn của trường lỗi
     *
     * @param propertyPath - đường dẫn của trường lỗi
     * @return String - trả về tên trường lỗi cuối cùng ( ví dụ: "name" trong "create.user.name")
     */
    private String getLastFieldFromPropertyPath(String propertyPath) {
        int lastDotIndex = propertyPath.lastIndexOf(".");
        return lastDotIndex != -1 ? propertyPath.substring(lastDotIndex + 1) : propertyPath;
    }
}
