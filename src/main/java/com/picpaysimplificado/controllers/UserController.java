package com.picpaysimplificado.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.picpaysimplificado.annotations.AuthenticatedUser;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.user.CreateUserDTO;
import com.picpaysimplificado.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController()
@RequestMapping("/users")
@Tag(name = "Users", description = "Operações relacionadas aos usuários")
public class UserController {

  @Autowired
  private UserService userService;

  @SuppressWarnings("null")
  @PostMapping("/")
  @Operation(summary = "Cria um novo usuário", description = "Cria um novo usuário no sistema com os dados fornecidos.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Dados inválidos"),
      @ApiResponse(responseCode = "409", description = "Usuário já existe")
  })
  public ResponseEntity<User> createUser(@RequestBody CreateUserDTO body, @AuthenticatedUser User user) {

    try {

      if (user == null) {
        System.out.println("entrou");
        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
      }

      User newUser = userService.createUser(body);

      return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/")
  @Operation(summary = "Buscar usuário", description = "Busca um usuário pelo ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
      @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
  })
  public ResponseEntity<List<User>> getAllUsers() {

    List<User> users = userService.getAllUsers();

    return new ResponseEntity<>(users, HttpStatus.OK);
  }
}
