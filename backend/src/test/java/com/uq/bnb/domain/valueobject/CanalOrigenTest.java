package com.uq.bnb.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

class CanalOrigenTest {
  @Test
  void defineLosCanalesPermitidos() {
    assertArrayEquals(
        new CanalOrigen[] {CanalOrigen.PORTAL, CanalOrigen.DIRECTO, CanalOrigen.EXTERNO},
        CanalOrigen.values());
  }
}
