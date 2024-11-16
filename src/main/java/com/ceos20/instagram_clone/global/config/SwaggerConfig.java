package com.ceos20.instagram_clone.global.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// TODO: Spring Security 작업 이후 수정 예정
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("인스타그램 클론 API") // Swagger 메인 타이틀
                .description("Ceos 인스타그램 클론 코딩 과제 API") // Swagger 설명
                .version("1.0.0"); // API의 버전
    }
}
