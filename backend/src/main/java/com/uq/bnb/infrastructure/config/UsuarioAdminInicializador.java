package com.uq.bnb.infrastructure.config;

import com.uq.bnb.domain.entity.Usuario;
import com.uq.bnb.domain.repository.UsuarioRepository;
import com.uq.bnb.domain.valueobject.RolUsuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UsuarioAdminInicializador implements ApplicationRunner {
  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final String email;
  private final String password;

  public UsuarioAdminInicializador(
      UsuarioRepository usuarioRepository,
      PasswordEncoder passwordEncoder,
      @Value("${app.security.admin.email:admin@uqbnb.test}") String email,
      @Value("${app.security.admin.password:Admin123!}") String password) {
    this.usuarioRepository = usuarioRepository;
    this.passwordEncoder = passwordEncoder;
    this.email = email.trim().toLowerCase();
    this.password = password;
  }

  @Override
  @Transactional
  public void run(ApplicationArguments args) {
    if (usuarioRepository.findByEmail(email).isPresent()) {
      return;
    }
    usuarioRepository.save(
        new Usuario(
            "Administrador de prueba", email, passwordEncoder.encode(password), RolUsuario.ADMIN));
  }
}
