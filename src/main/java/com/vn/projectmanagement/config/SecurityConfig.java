package com.vn.projectmanagement.config;

import com.vn.projectmanagement.security.JWT.JwtAuthEntryPoint;
import com.vn.projectmanagement.security.JWT.JwtAuthFilter;
import com.vn.projectmanagement.security.JWT.JwtTokenUtil;
import com.vn.projectmanagement.security.JWT.JwtUserDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetail jwtUserDetail;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JwtTokenUtil jwtTokenUtil, JwtUserDetail jwtUserDetail, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetail = jwtUserDetail;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    /**
     * Phương thức tạo ra một bean PasswordEncoder để mã hóa mật khẩu
     *
     * @return PasswordEncoder để mã hóa mật khẩu
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Phương thức tạo ra một bean JwtAuthFilter để xác thực người dùng trong quá trình xác thực và phân quyền
     *
     * @return JwtAuthFilter để xác thực người dùng trong quá trình xác thực và phân quyền
     */
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtTokenUtil, jwtUserDetail);
    }

    /**
     * Phương thức tạo ra một bean AuthenticationManager để xác thực người dùng trong quá trình xác thực và phân quyền
     *
     * @param authenticationConfiguration - đối tượng AuthenticationConfiguration
     * @return AuthenticationManager để xác thực người dùng
     * @throws Exception - nếu có lỗi xảy ra trong quá trình xác thực
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Phương thức tạo ra một bean SecurityFilterChain để xác thực người dùng trong quá trình xác thực và phân quyền cho người dùng truy cập vào các API của hệ thống thông qua các request
     * @param httpSecurity - đối tượng HttpSecurity
     * @return SecurityFilterChain để xác thực người dùng trong quá trình xác thực và phân quyền cho người dùng truy cập vào các API của hệ thống thông qua các request
     * @throws Exception - nếu có lỗi xảy ra trong quá trình xác thực
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Cấu hình cho HttpSecurity
        // - Tắt CSRF để tránh lỗi khi gửi request từ các trang web khác
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // - Cấu hình xử lý lỗi xác thực và phân quyền khi xác thực thất bại
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthEntryPoint)
                )
                .authorizeHttpRequests(registry -> registry
                        // Cấu hình cho các request được phép truy cập vào các API của hệ thống thông qua các request (các API được phép truy cập sẽ được cấu hình ở đây)
                        // và các request không được phép truy cập sẽ được chuyển hướng về trang xác thực hoặc trả về lỗi xác thực
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated() // Các request khác sẽ được yêu cầu xác thực
                );
        // Thêm JwtAuthFilter vào FilterChain để xác thực người dùng trong quá trình xác thực và phân quyền
        // Nếu không có dòng này, Spring Security sẽ không thể xác thực và phân quyền cho người dùng và sẽ không thể truy cập vào các API
        httpSecurity.addFilterBefore(jwtAuthFilter(), BasicAuthenticationFilter.class);
        return httpSecurity.build();
    }
}
