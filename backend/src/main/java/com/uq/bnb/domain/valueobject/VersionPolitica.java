package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.time.LocalDate;

public record VersionPolitica(int numero, LocalDate vigenteDesde) {
  public VersionPolitica {
    if (numero <= 0) {
      throw new ReglaDominioException("El número de versión de la política debe ser positivo.");
    }
    if (vigenteDesde == null) {
      throw new ReglaDominioException("La versión de la política debe indicar desde cuándo rige.");
    }
  }
}
