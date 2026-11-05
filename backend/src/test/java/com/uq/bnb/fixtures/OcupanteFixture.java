package com.uq.bnb.fixtures;

import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;

public final class OcupanteFixture {
  private OcupanteFixture() {}

  public static Ocupante crear() {
    return crear(TestIds.next(), "123456789", "Jhon Doe", LocalDate.of(1990, 1, 1));
  }

  public static Ocupante crear(ID id, String documento, String nombre, LocalDate fechaNacimiento) {
    return Ocupante.crear(id, documento, nombre, fechaNacimiento);
  }
}
