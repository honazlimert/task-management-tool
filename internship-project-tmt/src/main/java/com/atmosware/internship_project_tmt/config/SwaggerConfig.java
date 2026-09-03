package com.atmosware.internship_project_tmt.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        // yeni OpenAPI ayar nesnesi
        return new OpenAPI()
                // API genel bilgileri
                .info(new Info()
                        .title("Mini İş Takip Sistemi API")
                        .version("1.0")
                        .description("Atmosware Staj Projesi - Görev ve Proje Yönetimi API Dokümantasyonu"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                // yeni şema oluştur
                                new SecurityScheme()
                                        .name("bearerAuth")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Kullanıcı girişi yaptıktan sonra aldığınız token'ı buraya yapıştırın.")));
    }
}