package com.uq.bnb.infrastructure.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

// Firma y validación del JWT.
@Configuration
public class JwtConfig {
    private static final int BYTES_MINIMOS = 32;

    private final SecretKey clave;
    private final String emisor;

    public JwtConfig(
            @Value("${sga.jwt.secret}") String secreto, @Value("${sga.jwt.emisor}") String emisor) {
        byte[] bytes = secreto.getBytes(StandardCharsets.UTF_8);
        if (bytes.length < BYTES_MINIMOS) {
            throw new IllegalStateException(
                    "sga.jwt.secret debe tener al menos 32 caracteres (256 bits) para HS256");
        }
        this.clave = new SecretKeySpec(bytes, "HmacSHA256");
        this.emisor = emisor;
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(clave));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        // Algoritmo fijo (contra el algorithm swap) y validación de expiración y de emisor.
        NimbusJwtDecoder decoder =
                NimbusJwtDecoder.withSecretKey(clave).macAlgorithm(MacAlgorithm.HS256).build();
        decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer(emisor));
        return decoder;
    }
}
