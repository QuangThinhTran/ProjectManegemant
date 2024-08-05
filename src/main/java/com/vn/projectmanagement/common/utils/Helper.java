package com.vn.projectmanagement.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class Helper {

    /**
     * Set slug for string
     *
     * @param string - String to set slug
     * @return - Slug string
     */
    public static String setSlug(String string) {
        return string.replaceAll("[^a-zA-Z0-9.-]", "-").toLowerCase();
    }

    /**
     * Map response
     *
     * @param httpStatus - HttpStatus
     * @param message    - Message
     * @return - Map response
     */
    public static String mapResponse(HttpStatus httpStatus, String message) throws JsonProcessingException {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        responseBody.put("status", httpStatus.value());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(responseBody);
    }
}
