package com.picpaysimplificado.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.dtos.auth.LoginRequest;
import com.picpaysimplificado.repositories.UserRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public boolean validatePassword(String rawPassword, String encodedPassword) {
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  public String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  public User login(LoginRequest body) throws Exception {

    User user = userRepository.findUserByEmail(body.email())
        .orElseThrow(() -> new Exception("User not found with email: " + body.email()));

    if (!validatePassword(body.password(), user.getPassword())) {
      throw new Exception("Invalid password for user: " + body.email());
    }

    return user;
  }

  public void logout(HttpServletRequest request, HttpServletResponse response) {
    // Remove o cookie correto: "picpay-gabriel" em vez de "jwt"
    Cookie jwtCookie = new Cookie("picpay-gabriel", null);
    jwtCookie.setMaxAge(0);
    jwtCookie.setPath("/");
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(false);

    response.addCookie(jwtCookie);
    System.out.println("Cookie 'picpay-gabriel' removido");
  }
}
