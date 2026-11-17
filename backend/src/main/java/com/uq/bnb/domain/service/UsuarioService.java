package com.uq.bnb.domain.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.uq.bnb.domain.entity.Usuario;
import com.uq.bnb.domain.repository.UsuarioRepository;
import com.uq.bnb.domain.valueobject.ID;

@Service
public class UsuarioService {
  private final UsuarioRepository usuarioRepository;

  public UsuarioService(UsuarioRepository usuarioRepository) {
    this.usuarioRepository = usuarioRepository;
  }

  public Usuario crear(Usuario usuario) {
    return usuarioRepository.save(usuario);
  }

  public Optional<Usuario> obtenerPorId(ID id) {
    try {
      return usuarioRepository.findById(UUID.fromString(id.valor()));
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
  }
}
