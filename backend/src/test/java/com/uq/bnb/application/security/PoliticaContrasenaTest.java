package com.uq.bnb.application.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PoliticaContrasenaTest {
    @Test
    void aceptaUnaContrasenaConLongitudMayusculasYNumeros() {
        assertDoesNotThrow(() -> PoliticaContrasena.validar("Huesped2026"));
    }

    @Test
    void rechazaUnaContrasenaCorta() {
        assertThrows(IllegalArgumentException.class, () -> PoliticaContrasena.validar("Abc123"));
    }

    @Test
    void rechazaUnaContrasenaSinMayusculas() {
        assertThrows(IllegalArgumentException.class, () -> PoliticaContrasena.validar("huesped2026"));
    }

    @Test
    void rechazaUnaContrasenaSinNumeros() {
        assertThrows(IllegalArgumentException.class, () -> PoliticaContrasena.validar("HuespedSga"));
    }

    @Test
    void rechazaUnaContrasenaQueSuperaLosBytesQueBCryptProcesa() {
        String larga = "A1" + "x".repeat(71);

        assertThrows(IllegalArgumentException.class, () -> PoliticaContrasena.validar(larga));
    }

    @Test
    void rechazaUnaContrasenaNula() {
        assertThrows(NullPointerException.class, () -> PoliticaContrasena.validar(null));
    }
}
