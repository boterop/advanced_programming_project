package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public final class Salida {

  private final ID id;
  private final LocalDate fechaSalida;
  private final LocalTime horaSalida;
  private final CodigoReserva codigoReserva;
  private final String apartamentoId;
  private final String documentoTitular;

  private Salida(
      ID id,
      LocalDate fechaSalida,
      LocalTime horaSalida,
      CodigoReserva codigoReserva,
      String apartamentoId,
      String documentoTitular) {
    this.id = id;
    this.fechaSalida = fechaSalida;
    this.horaSalida = horaSalida;
    this.codigoReserva = codigoReserva;
    this.apartamentoId = apartamentoId;
    this.documentoTitular = documentoTitular;
  }

  public static Salida crear(
      ID id,
      LocalDate fechaSalida,
      LocalTime horaSalida,
      CodigoReserva codigoReserva,
      String apartamentoId,
      String documentoTitular) {
    if (fechaSalida == null) {
      throw new ReglaDominioException("La fecha de salida no puede ser vacia");
    }
    if (horaSalida == null) {
      throw new ReglaDominioException("La hora de salida no puede ser vacia");
    }
    if (codigoReserva == null) {
      throw new ReglaDominioException("La reserva no puede ser vacia");
    }
    if (apartamentoId == null || apartamentoId.isBlank()) {
      throw new ReglaDominioException("El apartamento no puede ser vacio");
    }
    if (documentoTitular == null || documentoTitular.isBlank()) {
      throw new ReglaDominioException("El titular no puede ser vacio");
    }
    return new Salida(id, fechaSalida, horaSalida, codigoReserva, apartamentoId, documentoTitular);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Salida entity && id.equals(entity.id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
