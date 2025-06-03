package com.picpaysimplificado.dtos;

import java.math.BigDecimal;

import com.picpaysimplificado.domain.user.UserType;

public record UserDTO(String id, String firstName, String lastName, String cpf, String email, String password,
    BigDecimal amount,
    UserType userType) {

  public UserDTO {
    if (firstName == null || firstName.isBlank()) {
      throw new IllegalArgumentException("First name cannot be null or blank");
    }
    if (lastName == null || lastName.isBlank()) {
      throw new IllegalArgumentException("Last name cannot be null or blank");
    }
    if (email == null || email.isBlank()) {
      throw new IllegalArgumentException("Email cannot be null or blank");
    }
    if (password == null || password.isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }
    if (userType == null) {
      throw new IllegalArgumentException("User type cannot be null");
    }
  }

}
