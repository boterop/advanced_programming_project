package com.uq.bnb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class JwtService {

  @Value("${app.security.jwt.secret}")
  private String secretKey;

  @Value("${app.security.jwt.expiration-minutes}")
  private int expirationMinutes;

  public String extractSubject(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Optional<String> extractValidSubject(String token) {
    try {
      String subject = extractSubject(token);
      if (isTokenValid(token, subject)) {
        return Optional.of(subject);
      }
    } catch (Exception e) {
      // Invalid token
    }
    return Optional.empty();
  }

  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  public String generateToken(String email) {
    return generateToken(new HashMap<>(), email);
  }

  public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
        .compact();
  }

  public String generateToken(Map<String, Object> extraClaims, String email) {
    return Jwts
        .builder()
        .setClaims(extraClaims)
        .setSubject(email)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)), SignatureAlgorithm.HS256)
        .compact();
  }

  public boolean isTokenValid(String token, String email) {
    final String tokenEmail = extractSubject(token);
    return (tokenEmail.equals(email) && !isTokenExpired(token));
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    return Jwts
        .parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
        .build()
        .parseClaimsJws(token)
        .getBody();
  }
}