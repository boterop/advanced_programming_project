package com.uq.bnb.domain.service;

import com.uq.bnb.application.dto.request.LoginRequest;
import com.uq.bnb.application.dto.request.RegisterRequest;
import com.uq.bnb.application.dto.response.AuthResponse;
import com.uq.bnb.application.dto.response.UsuarioResponse;
import com.uq.bnb.domain.entity.Usuario;
import com.uq.bnb.domain.exception.EmailAlreadyExistsException;
import com.uq.bnb.domain.exception.InvalidCredentialsException;
import com.uq.bnb.domain.repository.UsuarioRepository;
import com.uq.bnb.security.JwtService;
import java.util.Locale;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final String dummyHash;

  public AuthService(
      UsuarioRepository usuarioRepository,
      PasswordEncoder passwordEncoder,
      JwtService jwtService) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
    this.dummyHash = passwordEncoder.encode("dummy-password-for-timing");
  }

  @Transactional
  public AuthResponse register(RegisterRequest request) {
    String email = normalize(request.email());
    if (usuarioRepository.findByEmail(email).isPresent()) {
      throw new EmailAlreadyExistsException();
    }
    Usuario usuario = new Usuario(request.name().trim(), email, passwordEncoder.encode(request.password()));
    try {
      usuario = usuarioRepository.saveAndFlush(usuario);
    } catch (DataIntegrityViolationException ex) {
      throw new EmailAlreadyExistsException();
    }
    // Se firma usando la clave primaria ya asignada por la base de datos.
    return AuthResponse.bearer(
        jwtService.generateToken(usuario.id()), jwtService.getExpirationSeconds(), UsuarioResponse.from(usuario));
  }

  @Transactional(readOnly = true)
  public AuthResponse login(LoginRequest request) {
    String email = normalize(request.email());
    Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
    if (usuario == null || !passwordEncoder.matches(request.password(), usuario.password())) {
      if (usuario == null) passwordEncoder.matches(request.password(), dummyHash);
      throw new InvalidCredentialsException();
    }
    return AuthResponse.bearer(
        jwtService.generateToken(usuario.id()), jwtService.getExpirationSeconds(), UsuarioResponse.from(usuario));
  }

  @Transactional(readOnly = true)
  public UsuarioResponse obtenerPorId(String id) {
    try {
      return usuarioRepository.findById(Long.valueOf(id)).map(UsuarioResponse::from)
          .orElseThrow(InvalidCredentialsException::new);
    } catch (NumberFormatException ex) {
      throw new InvalidCredentialsException();
    }
  }

  private static String normalize(String email) {
    return email.trim().toLowerCase(Locale.ROOT);
  }
}
