package com.uq.bnb.security;

import com.uq.bnb.domain.entity.Usuario;
import com.uq.bnb.domain.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final String BEARER = "Bearer ";

  private final JwtService jwtService;
  private final UsuarioRepository usuarioRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
    this.jwtService = jwtService;
    this.usuarioRepository = usuarioRepository;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String token = null;
    String header = request.getHeader("Authorization");

    if (header != null
        && header.startsWith(BEARER)
        && SecurityContextHolder.getContext().getAuthentication() == null) {
      token = header.substring(BEARER.length()).trim();
    }
    if (token == null && request.getCookies() != null) {
      for (Cookie cookie : request.getCookies()) {
        if ("access_token".equals(cookie.getName())) {
          token = cookie.getValue();
          break;
        }
      }
    }

    if (token != null) {
      jwtService
          .extractValidSubject(token)
          .flatMap(this::findUser)
          .ifPresent(
              user -> {
                var auth =
                    new UsernamePasswordAuthenticationToken(
                        user.id(),
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
                SecurityContextHolder.getContext().setAuthentication(auth);
              });
    }
    chain.doFilter(request, response);
  }

  private java.util.Optional<Usuario> findUser(String subject) {
    try {
      return usuarioRepository.findById(java.util.UUID.fromString(subject));
    } catch (IllegalArgumentException exception) {
      return java.util.Optional.empty();
    }
  }
}
