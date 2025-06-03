package com.picpaysimplificado.domain.user;

import java.math.BigDecimal;

import com.picpaysimplificado.dtos.UserDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String firstName;
  private String lastName;

  @Column(unique = true)
  private String cpf;

  @Column(unique = true)
  private String email;

  private String password;

  private BigDecimal balance;

  @Enumerated(EnumType.STRING)
  private UserType userType;

  public User(UserDTO data) {
    this.firstName = data.firstName();
    this.lastName = data.lastName();
    this.cpf = data.cpf();
    this.email = data.email();
    this.password = data.password();
    this.balance = data.amount() != null ? data.amount() : BigDecimal.ZERO;
    this.userType = data.userType(); // Default user type
  }
}
