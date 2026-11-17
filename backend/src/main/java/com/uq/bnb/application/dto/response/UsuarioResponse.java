package com.uq.bnb.application.dto.response;

import com.uq.bnb.domain.entity.Usuario;

public record UsuarioResponse(String id, String name, String email) {

  public static UsuarioResponse from(Usuario user) {
    return new UsuarioResponse(user.id(), user.name(), user.email());
  }
}
