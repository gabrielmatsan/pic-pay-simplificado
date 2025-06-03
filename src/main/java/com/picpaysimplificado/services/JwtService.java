package com.picpaysimplificado.services;

import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.infra.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service JWT que inclui TODOS os dados do usuário no payload (exceto senha)
 */
@Service
public class JwtService {

  @Autowired
  private JwtConfig jwtConfig;

  /**
   * Extrai o email (subject) do token JWT
   * 
   * @param token - Token JWT válido
   * @return String - Email do usuário
   */
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  /**
   * Método genérico para extrair qualquer claim do token
   */
  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  /**
   * Gera token JWT com TODOS os dados do usuário
   * 
   * @param user - Objeto User completo
   * @return String - Token JWT com payload completo
   */
  public String generateToken(User user) {
    return createTokenWithAllUserData(user);
  }

  /**
   * ✅ MÉTODO PRINCIPAL: Cria token com TODOS os dados do usuário
   * INCLUI: id, firstName, lastName, email, cpf, balance, userType
   * EXCLUI: password (por segurança)
   */
  private String createTokenWithAllUserData(User user) {
    // Map com TODOS os dados do usuário (exceto senha)
    Map<String, Object> claims = new HashMap<>();

    // ✅ DADOS BÁSICOS
    claims.put("userId", user.getId());
    claims.put("firstName", user.getFirstName());
    claims.put("lastName", user.getLastName());
    claims.put("email", user.getEmail());
    claims.put("cpf", user.getCpf());

    // ✅ DADOS FINANCEIROS
    claims.put("balance", user.getBalance().toString()); // BigDecimal como String

    // ✅ DADOS DE TIPO
    claims.put("userType", user.getUserType().toString());

    // ✅ DADOS ADICIONAIS (se existirem)
    // claims.put("createdAt", user.getCreatedAt());
    // claims.put("updatedAt", user.getUpdatedAt());

    return Jwts.builder()
        .setClaims(claims) // Todos os dados do usuário
        .setSubject(user.getEmail()) // Subject = email principal
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
        .signWith(getSignInKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  /**
   * ✅ EXTRAI ID DO USUÁRIO
   */
  public String extractUserId(String token) {
    return extractClaim(token, claims -> claims.get("userId", String.class));
  }

  /**
   * ✅ EXTRAI PRIMEIRO NOME
   */
  public String extractFirstName(String token) {
    return extractClaim(token, claims -> claims.get("firstName", String.class));
  }

  /**
   * ✅ EXTRAI ÚLTIMO NOME
   */
  public String extractLastName(String token) {
    return extractClaim(token, claims -> claims.get("lastName", String.class));
  }

  /**
   * ✅ EXTRAI EMAIL
   */
  public String extractEmail(String token) {
    return extractClaim(token, claims -> claims.get("email", String.class));
  }

  /**
   * ✅ EXTRAI CPF
   */
  public String extractCpf(String token) {
    return extractClaim(token, claims -> claims.get("cpf", String.class));
  }

  /**
   * ✅ EXTRAI SALDO (retorna como BigDecimal)
   */
  public BigDecimal extractBalance(String token) {
    String balanceStr = extractClaim(token, claims -> claims.get("balance", String.class));
    return balanceStr != null ? new BigDecimal(balanceStr) : BigDecimal.ZERO;
  }

  /**
   * ✅ EXTRAI TIPO DE USUÁRIO
   */
  public String extractUserType(String token) {
    return extractClaim(token, claims -> claims.get("userType", String.class));
  }

  /**
   * ✅ MÉTODO ÚTIL: Reconstrói objeto User a partir do token
   * (sem senha, obviamente)
   */
  public User extractUserFromToken(String token) {
    User user = new User();
    user.setId(extractUserId(token));
    user.setFirstName(extractFirstName(token));
    user.setLastName(extractLastName(token));
    user.setEmail(extractEmail(token));
    user.setCpf(extractCpf(token));
    user.setBalance(extractBalance(token));
    // user.setUserType(UserType.valueOf(extractUserType(token)));

    return user;
  }

  /**
   * ✅ MÉTODO ÚTIL: Extrai TODOS os dados como Map
   */
  public Map<String, Object> extractAllUserData(String token) {
    Claims claims = extractAllClaims(token);
    Map<String, Object> userData = new HashMap<>();

    userData.put("userId", claims.get("userId"));
    userData.put("firstName", claims.get("firstName"));
    userData.put("lastName", claims.get("lastName"));
    userData.put("email", claims.get("email"));
    userData.put("cpf", claims.get("cpf"));
    userData.put("balance", claims.get("balance"));
    userData.put("userType", claims.get("userType"));

    return userData;
  }

  /**
   * Verifica se token é válido
   */
  public Boolean isTokenValid(String token, String email) {
    final String extractedEmail = extractUsername(token);
    return (extractedEmail.equals(email) && !isTokenExpired(token));
  }

  /**
   * Verifica se token expirou
   */
  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  /**
   * Extrai data de expiração
   */
  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  /**
   * Decodifica token e extrai todas as claims
   */
  private Claims extractAllClaims(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  /**
   * Gera chave criptográfica
   */
  private Key getSignInKey() {
    byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
    return Keys.hmacShaKeyFor(keyBytes);
  }
}