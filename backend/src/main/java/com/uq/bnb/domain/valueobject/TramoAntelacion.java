package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.math.BigDecimal;
import java.util.Objects;

public record TramoAntelacion(int antelacionMinimaHoras, BigDecimal porcentajeRetencion) {
  public TramoAntelacion {
    if (antelacionMinimaHoras < 0) {
      throw new ReglaDominioException("La antelación mínima no puede ser negativa");
    }
    Objects.requireNonNull(porcentajeRetencion, "El porcentaje de retención no puede ser nulo");
    if (porcentajeRetencion.compareTo(BigDecimal.ZERO) < 0
        || porcentajeRetencion.compareTo(BigDecimal.ONE) > 0) {
      throw new ReglaDominioException("El porcentaje de retención debe estar entre 0 y 1");
    }
  }

  public BigDecimal porcentajeDevolucion() {
    return BigDecimal.ONE.subtract(porcentajeRetencion);
  }

  public boolean aplicaPara(long horasDeAntelacion) {
    return horasDeAntelacion >= antelacionMinimaHoras;
  }
}
