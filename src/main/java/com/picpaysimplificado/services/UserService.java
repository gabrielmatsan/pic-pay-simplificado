package com.picpaysimplificado.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import com.picpaysimplificado.dtos.user.CreateUserDTO;
import com.picpaysimplificado.dtos.user.UserDTO;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AuthService authService;

  public void validateTransaction(User sender, BigDecimal amount) throws Exception {

    if (sender.getUserType() == UserType.MERCHANT) {
      throw new Exception("Merchant users cannot perform transactions.");
    }

    if (sender.getBalance().compareTo(amount) < 0) {
      throw new Exception("Insufficient balance for transaction.");
    }
  }

  public User findUserById(String id) throws Exception {
    return userRepository.findById(id).orElseThrow(() -> new Exception("User not found with id: " + id));
  }

  public void saveUser(User user) throws Exception {
    userRepository.save(user);
  }

  public User createUser(CreateUserDTO data) {
    User newUser = new User(data);

    // Validate the user data
    if (newUser.getFirstName() == null || newUser.getFirstName().isBlank()) {
      throw new IllegalArgumentException("First name cannot be null or blank");
    }
    if (newUser.getLastName() == null || newUser.getLastName().isBlank()) {
      throw new IllegalArgumentException("Last name cannot be null or blank");
    }
    if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
      throw new IllegalArgumentException("Email cannot be null or blank");
    }
    if (newUser.getPassword() == null || newUser.getPassword().isBlank()) {
      throw new IllegalArgumentException("Password cannot be null or blank");
    }

    String hashedPassword = authService.encodePassword(newUser.getPassword());

    newUser.setPassword(hashedPassword);

    return userRepository.save(newUser);
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
}