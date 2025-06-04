package com.picpaysimplificado.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.auth.LoginRequest;
import com.picpaysimplificado.dtos.user.UserResponseDTO;
import com.picpaysimplificado.services.AuthService;
import com.picpaysimplificado.services.CookieService;
import com.picpaysimplificado.services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController()
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Operações relacionadas a autenticação e autorização")
public class AuthController {

  @Autowired
  private AuthService authService;

  @Autowired
  private CookieService cookieService;

  @Autowired
  private JwtService jwtService;

  @PostMapping("/login")
  @Operation(summary = "Realiza o login", description = "Autentica o usuário e retorna um token JWT.")
  @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")

  public ResponseEntity<UserResponseDTO> login(
      @Valid @RequestBody LoginRequest body,
      HttpServletResponse response) {
    try {
      User user = authService.login(body);

      String token = jwtService.generateToken(user);

      cookieService.addJwtCookie(response, token);

      return ResponseEntity.ok(UserResponseDTO.fromUser(user));
    } catch (Exception e) {
      return ResponseEntity.status(401).body(null);
    }
  }

  /**
   * ✅ LOGOUT SIMPLIFICADO - Apenas clique
   * Token é lido automaticamente do cookie
   */
  @PostMapping("/logout")
  @Operation(summary = "Realiza o logout", description = "Remove cookie JWT automaticamente. Apenas clique, sem parâmetros.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso"),
      @ApiResponse(responseCode = "401", description = "Nenhum token encontrado"),
      @ApiResponse(responseCode = "400", description = "Token inválido")
  })
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
    try {
      authService.logout(request, response); // Passa os 2 parâmetros
      return ResponseEntity.ok("Logout successful - JWT cookie removed");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Logout failed: " + e.getMessage());
    }
  }
}
