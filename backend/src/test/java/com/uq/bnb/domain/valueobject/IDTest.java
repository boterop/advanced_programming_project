package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IDTest {
  private static final String ID_VALIDO = "2a79549d-4526-46b3-a4a4-fbde0b435352";

  @Test
  void creaIdConUuidValido() {
    assertEquals(ID_VALIDO, new ID(ID_VALIDO).valor());
  }

  @Test
  void eliminaEspaciosAlrededorDelUuid() {
    assertEquals(ID_VALIDO, new ID("  " + ID_VALIDO + "  ").valor());
  }

  @Test
  void rechazaIdsVaciosOMalFormados() {
    assertThrows(ReglaDominioException.class, () -> new ID("   "));
    assertThrows(ReglaDominioException.class, () -> new ID("no-es-un-uuid"));
  }

  @Test
  void comparaYCalculaHashPorValor() {
    ID primero = new ID(ID_VALIDO);
    ID igual = new ID(ID_VALIDO);
    ID diferente = new ID("98b0d3d0-9ea3-4b44-9f0d-17de32915201");

    assertEquals(primero, igual);
    assertEquals(primero.hashCode(), igual.hashCode());
    assertNotEquals(primero, diferente);
  }
}
