package com.picpaysimplificado.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.user.UserResponseDTO;
import com.picpaysimplificado.services.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController()
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Operações relacionadas a autenticação e autorização")
public class AuthController {

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  @Operation(summary = "Realiza o login", description = "Autentica o usuário e retorna um token JWT.")
  @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")

  public ResponseEntity<UserResponseDTO> login(String email, String password) {
    try {
      User user = authService.login(email, password);

      return ResponseEntity.ok(UserResponseDTO.fromUser(user));
    } catch (Exception e) {
      return ResponseEntity.status(401).body(null);
    }

  }
}
