package com.uq.bnb.application.security;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

// Contraseña segura del registro, debe tener mínimo 8 caracteres, con mayúsculas y números.
public final class PoliticaContrasena {
    private static final int LONGITUD_MINIMA = 8;
    // BCrypt solo procesa los primeros 72 bytes: se rechaza en vez de truncar en silencio.
    private static final int BYTES_MAXIMOS = 72;

    private PoliticaContrasena() {}

    public static void validar(String contrasena) {
        Objects.requireNonNull(contrasena, "La contraseña es obligatoria");
        boolean tieneMayuscula = contrasena.chars().anyMatch(Character::isUpperCase);
        boolean tieneNumero = contrasena.chars().anyMatch(Character::isDigit);
        if (contrasena.length() < LONGITUD_MINIMA || !tieneMayuscula || !tieneNumero) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener al menos 8 caracteres, con mayúsculas y números");
        }
        if (contrasena.getBytes(StandardCharsets.UTF_8).length > BYTES_MAXIMOS) {
            throw new IllegalArgumentException("La contraseña no puede superar los 72 bytes");
        }
    }
}
