package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.math.BigDecimal;
import java.util.Objects;

public record DescuentoEstanciaProlongada(int nochesMinimas, BigDecimal porcentaje) {
  public DescuentoEstanciaProlongada {
    if (nochesMinimas < 1) {
      throw new ReglaDominioException(
          "Las noches mínimas del descuento por estancia prolongada deben ser al menos 1");
    }
    Objects.requireNonNull(porcentaje, "El porcentaje de descuento no puede ser nulo");
    if (porcentaje.compareTo(BigDecimal.ZERO) < 0 || porcentaje.compareTo(BigDecimal.ONE) >= 0) {
      throw new ReglaDominioException(
          "El porcentaje de descuento debe ser mayor o igual a 0 y menor que 1");
    }
  }

  public boolean aplicaPara(long noches) {
    return noches >= nochesMinimas;
  }

  public Dinero aplicarA(Dinero tarifa) {
    Objects.requireNonNull(tarifa, "La tarifa no puede ser nula");
    return tarifa.multiplicar(BigDecimal.ONE.subtract(porcentaje));
  }
}
