package com.webcrafters.confease_backend.config; // 确保包名正确，最好建在 config 包下

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // 允许所有路径
                        .allowedOrigins("http://localhost:4200") // 明确允许前端地址
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                        .allowedHeaders("*") // 允许所有 Header
                        .allowCredentials(true); // 允许携带 Cookie/认证信息
            }
        };
    }
}