package com.uq.bnb.fixtures;

import com.uq.bnb.application.security.CuentaUsuario;
import com.uq.bnb.application.security.RolUsuario;

import java.time.LocalDate;

public class UsuarioFixture {
    public static final String CONTRASENA = "Huesped2026";

    private UsuarioFixture() {}

    public static CuentaUsuario crear() {
        return crear("huesped1@sga.local", "1094000111", RolUsuario.HUESPED);
    }

    // La contraseña cifrada es la del {@link FakeCodificadorContrasena}: "cifrada:Huesped2026".
    public static CuentaUsuario crear(String correo, String documento, RolUsuario rol) {
        return CuentaUsuario.crear(
                TestIds.next(),
                "Laura Gomez",
                correo,
                "cifrada:" + CONTRASENA,
                "3001000004",
                documento,
                LocalDate.of(1995, 1, 30),
                rol);
    }
}
