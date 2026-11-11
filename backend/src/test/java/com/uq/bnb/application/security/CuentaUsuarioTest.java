package com.uq.bnb.application.security;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.fixtures.TestIds;
import com.uq.bnb.fixtures.UsuarioFixture;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CuentaUsuarioTest {
    private static CuentaUsuario crear(String correo, String telefono, LocalDate nacimiento) {
        return CuentaUsuario.crear(
                TestIds.next(),
                "Laura Gomez",
                correo,
                "cifrada:Huesped2026",
                telefono,
                "1094000111",
                nacimiento,
                RolUsuario.HUESPED);
    }

    @Test
    void nacActivoYNormalizaElCorreoYElTelefono() {
        CuentaUsuario cuenta = crear("  Laura@SGA.local ", "300 100-0004", LocalDate.of(1995, 1, 30));

        assertTrue(cuenta.activo());
        assertEquals("laura@sga.local", cuenta.correo());
        assertEquals("3001000004", cuenta.telefono());
    }

    @Test
    void rechazaUnCorreoSinFormatoValido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> crear("sin-arroba", "3001000004", LocalDate.of(1995, 1, 30)));
    }

    @Test
    void rechazaUnTelefonoSinFormatoValido() {
        assertThrows(
                IllegalArgumentException.class,
                () -> crear("laura@sga.local", "abc", LocalDate.of(1995, 1, 30)));
    }

    @Test
    void rechazaUnaFechaDeNacimientoFutura() {
        assertThrows(
                IllegalArgumentException.class,
                () -> crear("laura@sga.local", "3001000004", LocalDate.now().plusDays(1)));
    }

    @Test
    void rechazaUnDocumentoMasLargoQueLaColumna() {
        assertThrows(
                IllegalArgumentException.class,
                () ->
                        CuentaUsuario.crear(
                                TestIds.next(),
                                "Laura Gomez",
                                "laura@sga.local",
                                "cifrada:Huesped2026",
                                "3001000004",
                                "1234567890123456",
                                LocalDate.of(1995, 1, 30),
                                RolUsuario.HUESPED));
    }

    @Test
    void desactivarDevuelveUnaCopiaInactivaYNoAlteraLaOriginal() {
        CuentaUsuario cuenta = UsuarioFixture.crear();

        CuentaUsuario inactiva = cuenta.desactivar();

        assertFalse(inactiva.activo());
        assertTrue(cuenta.activo());
        assertEquals(cuenta, inactiva);
    }

    @Test
    void noSePuedeDesactivarDosVeces() {
        CuentaUsuario inactiva = UsuarioFixture.crear().desactivar();

        assertThrows(ReglaDominioException.class, inactiva::desactivar);
    }

    @Test
    void dosCuentasConDistintoIdentificadorNoSonIguales() {
        CuentaUsuario a = UsuarioFixture.crear();
        CuentaUsuario b = UsuarioFixture.crear();

        assertNotEquals(a, b);
    }

    @Test
    void laRepresentacionEnTextoNoExponeLaContrasenaCifrada() {
        CuentaUsuario cuenta = UsuarioFixture.crear();

        assertFalse(cuenta.toString().contains("cifrada"));
    }

    @Test
    void rechazaUnIdentificadorNulo() {
        ID sinId = null;

        assertThrows(
                NullPointerException.class,
                () ->
                        CuentaUsuario.crear(
                                sinId,
                                "Laura Gomez",
                                "laura@sga.local",
                                "cifrada:Huesped2026",
                                "3001000004",
                                "1094000111",
                                LocalDate.of(1995, 1, 30),
                                RolUsuario.HUESPED));
    }
}
