package com.vn.projectmanagement.security.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetail jwtUserDetails;

    public JwtAuthFilter(JwtTokenUtil jwtTokenUtil, JwtUserDetail jwtUserDetails) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetails = jwtUserDetails;
    }

    /**
     * Xử lý request và response
     *
     * @param request     - HttpServletRequest đối tượng dùng để nhận request
     * @param response    - HttpServletResponse đối tượng dùng để trả về response
     * @param filterChain - FilterChain đôi tượng dùng để chuyển tiếp request và response
     * @throws ServletException - nếu có lỗi xảy ra trong quá trình xử lý request
     * @throws IOException      - nếu có lỗi xảy ra trong quá trình xử lý request
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = this.getJwtTokenFormRequest(request);
        if (token != null && this.jwtTokenUtil.validateToken(token)) {
            // Lấy username từ token
            String username = this.jwtTokenUtil.extractUsername(token);

            // Lấy thông tin người dùng từ username
            UserDetails userDetails = this.jwtUserDetails.loadUserByUsername(username);

            // Tạo ra authentication token lưu thông tin người dùng vào SecurityContextHolder để sử dụng trong quá trình xác thực và phân quyền
            // Nếu không có dòng này, Spring Security sẽ không thể xác thực và phân quyền cho người dùng và sẽ không thể truy cập vào các API
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Chuyển tiếp request và response để xử lý tiếp trong các Filter tiếp theo và cuối cùng là Controller
        filterChain.doFilter(request, response);
    }


    /**
     * Lấy JWT token từ request
     *
     * @param request - HttpServletRequest đối tượng dùng để nhận request
     * @return JWT token từ request nếu tồn tại, ngược lại trả về null
     */
    private String getJwtTokenFormRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
