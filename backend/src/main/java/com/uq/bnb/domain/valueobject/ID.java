package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.util.UUID;

public record ID(String valor) {
  public ID {

    if (valor == null || valor.isBlank()) {
      throw new ReglaDominioException("El ID no es valido");
    }
    valor = valor.trim();
    try {
      UUID uuid = UUID.fromString(valor);
      if (!uuid.toString().equalsIgnoreCase(valor)) {
        throw new ReglaDominioException("El ID no es valido");
      }
    } catch (IllegalArgumentException exception) {
      throw new ReglaDominioException("El ID no es valido");
    }
  }
}
