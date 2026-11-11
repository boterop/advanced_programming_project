package com.uq.bnb.infrastructure.security;

import com.uq.bnb.application.port.CodificadorContrasena;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

// Cifra con BCrypt. El codificador delegante guarda el algoritmo junto al hash ({bcrypt}$2a$...)
@Component
public class BCryptCodificadorContrasena implements CodificadorContrasena {
    private static final int LIMITE_BYTES = 72;

    private final PasswordEncoder encoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public String codificar(String contrasena) {
        Objects.requireNonNull(contrasena, "La contraseña es obligatoria");
        if (superaLimite(contrasena)) {
            throw new IllegalArgumentException("La contraseña no puede superar los 72 bytes");
        }
        return Objects.requireNonNull(encoder.encode(contrasena), "BCrypt no devolvió un hash");
    }

    @Override
    public boolean coincide(String contrasena, String cifrada) {
        return !superaLimite(contrasena) && encoder.matches(contrasena, cifrada);
    }

    private static boolean superaLimite(String contrasena) {
        return contrasena.getBytes(StandardCharsets.UTF_8).length > LIMITE_BYTES;
    }
}
