package com.uq.bnb.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.UUID;
import com.uq.bnb.domain.valueobject.RolUsuario;

@Getter
@Entity
@Table(name = "usuarios")
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RolUsuario role;

  protected Usuario() {
  }

  public Usuario(String name, String email, String password) {
    this(name, email, password, RolUsuario.CLIENTE);
  }

  public Usuario(String name, String email, String password, RolUsuario role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public void updatePassword(String newPassword, PasswordEncoder passwordEncoder) {
    if (newPassword == null || newPassword.isBlank()) {
      throw new IllegalArgumentException("La nueva contraseña no puede estar vacía");
    }

    if (passwordEncoder == null) {
      throw new IllegalArgumentException("PasswordEncoder no puede ser null");
    }

    this.password = passwordEncoder.encode(newPassword);
  }

  public String id() { return id == null ? null : id.toString(); }
  public String name() { return name; }
  public String email() { return email; }
  public String password() { return password; }
  public RolUsuario getRole() { return role; }
}
