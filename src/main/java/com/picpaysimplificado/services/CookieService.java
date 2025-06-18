package com.picpaysimplificado.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

  @Value("${jwt.expiration:86400000}")
  private long jwtExpiration;

  /**
   * ✅ Adiciona token JWT como cookie HttpOnly
   */
  public void addJwtCookie(HttpServletResponse response, String token) {
    Cookie jwtCookie = new Cookie("picpay-gabriel", token);

    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(false);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge((int) (jwtExpiration / 1000));
    jwtCookie.setAttribute("SameSite", "Strict");

    response.addCookie(jwtCookie);
    System.out.println("JWT cookie adicionado com sucesso");
  }

  /**
   * ✅ Extrai token JWT do cookie
   */
  public String getJwtFromCookie(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("picpay-gabriel".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }

  /**
   * ✅ Remove cookie JWT (logout)
   */
  public void removeJwtCookie(HttpServletResponse response) {
    Cookie jwtCookie = new Cookie("picpay-gabriel", "");
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(false);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(0); // ✅ Expira imediatamente

    response.addCookie(jwtCookie);
    System.out.println("JWT cookie removido");
  }
}