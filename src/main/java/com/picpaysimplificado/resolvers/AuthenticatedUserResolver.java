package com.picpaysimplificado.resolvers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.picpaysimplificado.annotations.AuthenticatedUser;
import com.picpaysimplificado.services.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolver responsável por processar a anotação @AuthenticatedUser
 * e extrair o ID do usuário do JWT cookie automaticamente.
 */
@Component
public class AuthenticatedUserResolver implements HandlerMethodArgumentResolver {

  @Autowired
  private JwtService jwtService;

  /**
   * Verifica se este resolver deve processar o parâmetro
   */
  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.hasParameterAnnotation(AuthenticatedUser.class) &&
        parameter.getParameterType().equals(String.class);
  }

  /**
   * Extrai o userId do JWT cookie e retorna para o parâmetro anotado
   */
  @Override
  public Object resolveArgument(MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) throws Exception {

    HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

    if (request == null) {
      System.out.println("❌ Request é null");
      return null;
    }

    // Busca o JWT no cookie
    String jwtToken = extractJwtFromCookie(request);

    if (jwtToken == null) {
      System.out.println("⚠️ JWT não encontrado no cookie");
      return null;
    }

    try {
      // Extrai o userId do JWT
      String userId = jwtService.extractUserId(jwtToken);
      System.out.println("✅ UserId extraído do JWT: " + userId);
      return userId;
    } catch (Exception e) {
      System.out.println("❌ Erro ao extrair userId do JWT: " + e.getMessage());
      return null;
    }
  }

  /**
   * Extrai o JWT do cookie
   */
  private String extractJwtFromCookie(HttpServletRequest request) {
    if (request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        // Usa o nome do seu cookie JWT
        if ("picpay-gabriel".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}