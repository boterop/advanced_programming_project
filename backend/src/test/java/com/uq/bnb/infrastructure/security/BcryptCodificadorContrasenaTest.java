package com.uq.bnb.infrastructure.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BcryptCodificadorContrasenaTest {
    private final BCryptCodificadorContrasena codificador = new BCryptCodificadorContrasena();

    @Test
    void cifraConBCryptYNuncaDevuelveElTextoPlano() {
        String cifrada = codificador.codificar("Huesped2026");

        assertTrue(cifrada.startsWith("{bcrypt}"));
        assertFalse(cifrada.contains("Huesped2026"));
    }

    @Test
    void laMismaContrasenaSeCifraDistintoCadaVezPorLaSal() {
        assertNotEquals(codificador.codificar("Huesped2026"), codificador.codificar("Huesped2026"));
    }

    @Test
    void reconoceLaContrasenaCorrectaYRechazaLaErrada() {
        String cifrada = codificador.codificar("Huesped2026");

        assertTrue(codificador.coincide("Huesped2026", cifrada));
        assertFalse(codificador.coincide("Huesped2027", cifrada));
    }

    @Test
    void rechazaCifrarUnaContrasenaQueSuperaLosBytesQueBCryptProcesa() {
        String larga = "A1" + "x".repeat(71);

        assertThrows(IllegalArgumentException.class, () -> codificador.codificar(larga));
        assertFalse(codificador.coincide(larga, codificador.codificar("Huesped2026")));
    }
}
