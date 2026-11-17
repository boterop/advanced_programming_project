package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.util.List;
import java.util.Objects;

public record Cotizacion(List<DetalleNoche> desglosePorNoche) {
  public Cotizacion {
    Objects.requireNonNull(desglosePorNoche, "El desglose no puede ser nulo");
    if (desglosePorNoche.isEmpty()) {
      throw new ReglaDominioException(
          "Una cotización debe tener al menos una noche en su desglose");
    }
    desglosePorNoche = List.copyOf(desglosePorNoche);
  }

  public Dinero valorTotal() {
    return desglosePorNoche.stream()
        .map(DetalleNoche::subtotal)
        .reduce(Dinero.cero(), Dinero::sumar);
  }

  public int totalNoches() {
    return desglosePorNoche.size();
  }
}
