package com.vn.projectmanagement.security.JWT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vn.projectmanagement.common.swagger.SwaggerMessages;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Xử lý request và response khi xác thực thất bại
     *
     * @param request       - HttpServletRequest đối tượng dùng để nhận request
     * @param response      - HttpServletResponse đối tượng dùng để trả về response
     * @param authException - AuthenticationException đối tượng dùng để xử lý lỗi xác thực
     * @throws IOException      - nếu có lỗi xảy ra trong quá trình xử lý request
     * @throws ServletException - nếu có lỗi xảy ra trong quá trình xử lý request
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
        // Đặt status code của response là 403 (FORBIDDEN)
        // Đặt kiểu dữ liệu của response là JSON
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        // Tạo một Map để biểu diễn phần thân của response
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.FORBIDDEN.value());
        responseBody.put("message", SwaggerMessages.INVALID_JWT_TOKEN);

        // Chuyển Map thành chuỗi JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseBody);

        // Trả về thông báo lỗi khi token không hợp lệ
        response.getWriter().write(jsonResponse);
    }
}
