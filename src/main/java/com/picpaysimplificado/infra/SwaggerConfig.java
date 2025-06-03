package com.picpaysimplificado.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("PicPay Simplificado API")
            .version("1.0.0")
            .description("API para sistema de pagamentos simplificado")
            .contact(new Contact()
                .name("Seu Nome")
                .email("seu@email.com")));
  }
}