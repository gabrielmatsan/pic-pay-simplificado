package com.picpaysimplificado.dtos.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de login
 */
@Schema(description = "Dados para login")
public record LoginRequest(

    @NotBlank(message = "Email is required") @Email(message = "Email must be valid") @Schema(description = "Email do usuário", example = "joao@example.com") String email,

    @NotBlank(message = "Password is required") @Schema(description = "Senha do usuário", example = "minhasenha123") String password) {
}