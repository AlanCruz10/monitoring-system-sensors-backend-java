package org.app.mss.security.configurations;

import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class ConfigurationCORSSecurity {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS","HEAD")
                        .allowedHeaders("*")
                        .exposedHeaders("*");
            }
        };
    }

}