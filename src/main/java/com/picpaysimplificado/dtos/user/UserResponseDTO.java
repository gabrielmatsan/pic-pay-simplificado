package com.picpaysimplificado.dtos.user;

import java.math.BigDecimal;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO para RESPOSTA (sem senha)
 */
@Schema(description = "Dados do usuário (sem senha)")
public record UserResponseDTO(
        @Schema(description = "ID do usuário") String id,

        @Schema(description = "Primeiro nome") String firstName,

        @Schema(description = "Último nome") String lastName,

        @Schema(description = "CPF") String cpf,

        @Schema(description = "Email") String email,

        @Schema(description = "Saldo") BigDecimal balance,

        @Schema(description = "Tipo do usuário") UserType userType) {
    /**
     * Converte User entity para UserResponseDTO
     */
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getCpf(),
                user.getEmail(),
                user.getBalance(),
                user.getUserType());
    }
}