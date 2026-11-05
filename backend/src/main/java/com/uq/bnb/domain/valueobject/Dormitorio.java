package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;

public record Dormitorio(String descripcion, String tipoCama) {
  public Dormitorio {
    if (descripcion == null || descripcion.isBlank()) {
      throw new ReglaDominioException("La descripción del dormitorio no puede estar vacía");
    }
    if (tipoCama == null || tipoCama.isBlank()) {
      throw new ReglaDominioException("El tipo de cama del dormitorio no puede estar vacío");
    }
  }
}
