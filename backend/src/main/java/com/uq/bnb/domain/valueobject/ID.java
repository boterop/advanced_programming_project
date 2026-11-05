package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;

public record ID(String valor) {
  public ID

  {
    if (valor == null || valor.isBlank()) {
      throw new ReglaDominioException("El ID no es valido");
    }
    valor = valor.trim();
    if (!valor.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}")) {
      throw new ReglaDominioException("El ID no es valido");
    }
  }
}
