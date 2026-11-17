package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.application.dto.request.LoginRequest;
import com.uq.bnb.application.dto.request.RegisterRequest;
import com.uq.bnb.application.dto.response.AuthResponse;
import com.uq.bnb.application.dto.response.UsuarioResponse;
import com.uq.bnb.domain.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @PostMapping("/register")
  public ResponseEntity<AuthResponse> register(
      @Valid @RequestBody RegisterRequest request, HttpServletResponse response) {
    AuthResponse auth = authService.register(request);
    addTokenCookie(response, auth.token());
    return ResponseEntity.status(HttpStatus.CREATED).body(auth);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
    AuthResponse auth = authService.login(request);
    addTokenCookie(response, auth.token());
    return ResponseEntity.ok(auth);
  }

  @GetMapping("/me")
  public UsuarioResponse me(Authentication authentication) {
    return authService.obtenerPorId(authentication.getName());
  }

  @PostMapping("/logout")
  public ResponseEntity<Void> logout(HttpServletResponse response) {
    Cookie cookie = new Cookie("access_token", "");
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(0);
    response.addCookie(cookie);
    return ResponseEntity.noContent().build();
  }

  private static void addTokenCookie(HttpServletResponse response, String token) {
    Cookie cookie = new Cookie("access_token", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60);
    response.addCookie(cookie);
  }
}
