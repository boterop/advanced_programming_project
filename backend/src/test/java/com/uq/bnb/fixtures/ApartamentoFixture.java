package com.uq.bnb.fixtures;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.EstadoOperativo;
import java.util.List;

public final class ApartamentoFixture {
  private ApartamentoFixture() {}

  public static Apartamento crear() {
    return Apartamento.crear(
        TestIds.next(),
        "APT-001",
        "Apartamento de prueba",
        4,
        List.of(new Dormitorio("Dormitorio 1", "Cama doble")),
        EstadoOperativo.PREPARADO);
  }
}
