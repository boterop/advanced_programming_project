package com.uq.bnb.application.dto.response;

public record AuthResponse(
    String token, String tokenType, long expiresInSeconds, UsuarioResponse user) {

  public static AuthResponse bearer(String token, long expiresInSeconds, UsuarioResponse user) {
    return new AuthResponse(token, "Bearer", expiresInSeconds, user);
  }
}
