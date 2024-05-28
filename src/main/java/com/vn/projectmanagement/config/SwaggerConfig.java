package com.vn.projectmanagement.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    // Tên của SecurityScheme sử dụng trong Swagger UI
    public static final String SECURITY_SCHEME_NAME = "CONCONGSAN";

    /**
     * Phương thức tạo ra một bean OpenAPI để cấu hình thông tin cho Swagger UI (tên, phiên bản, ...) và cấu hình bảo mật cho Swagger UI (sử dụng JWT)
     * @return OpenAPI - Bean để cấu hình thông tin cho Swagger UI (tên, phiên bản, ...)
     */
    @Bean
    public OpenAPI openAPI() {
        // Cấu hình bảo mật cho Swagger UI (sử dụng JWT)
        // Để sử dụng JWT, ta cần thêm một SecurityScheme vào Components của OpenAPI
        // SecurityScheme này sẽ được sử dụng trong các API để xác thực người dùng sử dụng JWT
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme()))
                .info(new Info()
                        .title("ProjectManagement API")
                        .version("1.0"));
    }

    /**
     * Phương thức tạo ra một bean GroupedOpenApi để cấu hình nhóm API cho Swagger UI (public, private, ...)
     * @return GroupedOpenApi - Bean để cấu hình nhóm API cho Swagger UI
     */
    @Bean
    public GroupedOpenApi publicApi() {
        // Cấu hình nhóm API cho Swagger UI (public)
        // Nhóm API này sẽ chứa các API mà không cần xác thực người dùng
        // Các API trong nhóm này sẽ không cần sử dụng JWT để xác thực người dùng
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**", "/swagger-ui/**", "/v3/api-docs/**")
                .build();
    }

    /**
     * Phương thức tạo ra một bean GroupedOpenApi để cấu hình nhóm API cho Swagger UI (public, private, ...)
     * @return SecurityScheme - Bean để cấu hình bảo mật cho Swagger UI (sử dụng JWT)
     */
    private SecurityScheme securityScheme() {
        // Cấu hình bảo mật cho Swagger UI (sử dụng JWT)
        // Để sử dụng JWT, ta cần thêm một SecurityScheme vào Components của OpenAPI
        // SecurityScheme này sẽ được sử dụng trong các API để xác thực người dùng sử dụng JWT
        // Cấu hình loại bảo mật là HTTP, scheme là bearer, bearerFormat là JWT
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setType(SecurityScheme.Type.HTTP);
        securityScheme.setScheme("bearer");
        securityScheme.bearerFormat("JWT");
        return securityScheme;
    }
}
