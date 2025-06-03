package com.picpaysimplificado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.repositories.UserRepository;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  public boolean validatePassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  public String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  public User login(String email, String password) throws Exception {

    User user = userRepository.findUserByEmail(email)
        .orElseThrow(() -> new Exception("User not found with email: " + email));

    if (!validatePassword(password, user.getPassword())) {
      throw new Exception("Invalid password for user: " + email);
    }

    jwtService.generateToken(user);

    return user;
  }

}
