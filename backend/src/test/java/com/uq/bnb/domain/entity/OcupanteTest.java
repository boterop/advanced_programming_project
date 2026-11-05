package com.uq.bnb.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uq.bnb.fixtures.OcupanteFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class OcupanteTest {
  @Test
  void conservaDatosYCalculaEdadEnLaFechaSolicitada() {
    LocalDate nacimiento = LocalDate.of(2000, 6, 15);
    Ocupante ocupante =
        OcupanteFixture.crear(
            com.uq.bnb.fixtures.TestIds.next(), "123456789", "Ana Torres", nacimiento);

    assertEquals("123456789", ocupante.documento());
    assertEquals("Ana Torres", ocupante.nombre());
    assertEquals(30, ocupante.edad(LocalDate.of(2030, 6, 15)));
  }

  @Test
  void determinaSiEsFacturableSegunEdadDeCorte() {
    Ocupante ocupante =
        OcupanteFixture.crear(
            com.uq.bnb.fixtures.TestIds.next(),
            "123456789",
            "Ana Torres",
            LocalDate.of(2020, 6, 15));

    assertTrue(ocupante.esFacturable(LocalDate.of(2030, 6, 15), 5));
    assertFalse(ocupante.esFacturable(LocalDate.of(2023, 6, 15), 5));
  }
}
