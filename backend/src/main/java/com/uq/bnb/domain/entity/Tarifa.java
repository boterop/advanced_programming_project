package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class Tarifa {

  private final ID id;
  private final ID temporadaId;
  private final Dinero valorPorNoche;
  private final LocalDate vigenteDesde;

  private Tarifa(ID id, ID temporadaId, Dinero valorPorNoche, LocalDate vigenteDesde) {
    this.id = id;
    this.temporadaId = temporadaId;
    this.valorPorNoche = valorPorNoche;
    this.vigenteDesde = vigenteDesde;
  }

  public static Tarifa crear(ID id, ID temporadaId, Dinero valorPorNoche, LocalDate vigenteDesde) {
    Objects.requireNonNull(temporadaId, "El identificador de la temporada no puede ser nulo");
    Objects.requireNonNull(valorPorNoche, "El valor por noche no puede ser nulo");
    Objects.requireNonNull(vigenteDesde, "La fecha de vigencia no puede ser nula");
    if (valorPorNoche.esNegativo() || valorPorNoche.esCero()) {
      throw new ReglaDominioException("El valor de una tarifa debe ser positivo");
    }
    return new Tarifa(id, temporadaId, valorPorNoche, vigenteDesde);
  }

  public static Tarifa reconstruir(
      ID id, ID temporadaId, Dinero valorPorNoche, LocalDate vigenteDesde) {
    return new Tarifa(id, temporadaId, valorPorNoche, vigenteDesde);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Tarifa entity && id.equals(entity.id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
