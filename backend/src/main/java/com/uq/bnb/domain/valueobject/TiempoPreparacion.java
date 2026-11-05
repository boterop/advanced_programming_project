package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;

public record TiempoPreparacion(int horas) {
  public TiempoPreparacion {
    if (horas < 0) {
      throw new ReglaDominioException("El tiempo de preparación no puede ser negativo.");
    }
  }
}
