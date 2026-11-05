package com.uq.bnb;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uq.bnb.application.port.IDProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectApplicationTests {
  @Autowired private IDProvider idProvider;

  @Test
  void obtieneIdentificadoresUnicosDesdeLaBaseDeDatos() {
    String primero = idProvider.obtener().valor();
    String segundo = idProvider.obtener().valor();

    assertNotEquals(primero, segundo);
    assertTrue(primero.matches("[0-9a-fA-F-]{36}"));
    assertTrue(segundo.matches("[0-9a-fA-F-]{36}"));
  }
}
