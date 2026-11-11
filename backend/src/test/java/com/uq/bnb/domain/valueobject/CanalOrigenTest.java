package com.uq.bnb.domain.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CanalOrigenTest {
  @Test
  void defineLosCanalesPermitidos() {
    assertArrayEquals(
        new CanalOrigen[] {CanalOrigen.PORTAL, CanalOrigen.DIRECTO, CanalOrigen.EXTERNO},
        CanalOrigen.values());
  }
}
