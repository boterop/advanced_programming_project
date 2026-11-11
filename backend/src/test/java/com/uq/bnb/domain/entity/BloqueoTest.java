package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.fixtures.BloqueoFixture;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BloqueoTest {
  @Test
  void detectaSolapamientoConElRangoBloqueado() {
    Bloqueo bloqueo = BloqueoFixture.crear();
    LocalDate inicio = bloqueo.rango().entrada();

    assertTrue(bloqueo.seSolapaCon(new Estancia(inicio.plusDays(1), inicio.plusDays(2))));
    assertFalse(bloqueo.seSolapaCon(new Estancia(inicio.plusDays(3), inicio.plusDays(5))));
  }
}
