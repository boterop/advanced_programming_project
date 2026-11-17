package com.uq.bnb.application.dto.response;

import com.uq.bnb.domain.entity.Usuario;
import com.uq.bnb.domain.valueobject.RolUsuario;

public record UsuarioResponse(String id, String name, String email, RolUsuario role) {

  public static UsuarioResponse from(Usuario user) {
    return new UsuarioResponse(user.id(), user.name(), user.email(), user.getRole());
  }
}
