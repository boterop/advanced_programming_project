package com.uq.bnb.infrastructure.security;

import com.uq.bnb.application.security.TokenEmitido;
import com.uq.bnb.infrastructure.config.JwtConfig;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwrTokenEmisorTest {
    private static final String SECRETO = "clave-de-prueba-con-mas-de-treinta-y-dos-caracteres-0123";
    private static final String EMISOR = "sga";

    private static JwtTokenEmisor emisor(String secreto, long minutos) {
        return new JwtTokenEmisor(new JwtConfig(secreto, EMISOR).jwtEncoder(), EMISOR, minutos);
    }

    private static JwtDecoder decodificador(String secreto, String emisor) {
        return new JwtConfig(secreto, emisor).jwtDecoder();
    }

    @Test
    void emiteUnTokenFirmadoConLosClaimsDelUsuario() {
        TokenEmitido emitido =
                emisor(SECRETO, 30).emitir("huesped1@sga.local", "1094000111", List.of("HUESPED"));

        Jwt jwt = decodificador(SECRETO, EMISOR).decode(emitido.token());

        assertEquals("huesped1@sga.local", jwt.getSubject());
        assertEquals("1094000111", jwt.getClaimAsString("documento"));
        assertEquals(List.of("HUESPED"), jwt.getClaimAsStringList("roles"));
        assertEquals(EMISOR, jwt.getIssuer().toString());
        assertEquals(emitido.expiraEn(), jwt.getExpiresAt());
    }

    @Test
    void rechazaUnTokenAlteradoAMano() {
        String token = emisor(SECRETO, 30).emitir("a@sga.local", "1", List.of("HUESPED")).token();
        String alterado = token.substring(0, token.length() - 4) + "AAAA";

        assertNotEquals(token, alterado);
        assertThrows(JwtException.class, () -> decodificador(SECRETO, EMISOR).decode(alterado));
    }

    @Test
    void rechazaUnTokenFirmadoConOtraClave() {
        String token = emisor(SECRETO, 30).emitir("a@sga.local", "1", List.of("HUESPED")).token();
        String otraClave = "otra-clave-distinta-con-mas-de-treinta-y-dos-caracteres-99";

        assertThrows(JwtException.class, () -> decodificador(otraClave, EMISOR).decode(token));
    }

    @Test
    void rechazaUnTokenVencido() {
        String token = emisor(SECRETO, -10).emitir("a@sga.local", "1", List.of("HUESPED")).token();

        assertThrows(JwtException.class, () -> decodificador(SECRETO, EMISOR).decode(token));
    }

    @Test
    void rechazaUnTokenDeOtroEmisor() {
        String token = emisor(SECRETO, 30).emitir("a@sga.local", "1", List.of("HUESPED")).token();

        assertThrows(JwtException.class, () -> decodificador(SECRETO, "otro-emisor").decode(token));
    }

    @Test
    void noArrancaConUnaClaveDeMenosDe32Caracteres() {
        assertThrows(IllegalStateException.class, () -> new JwtConfig("corta", EMISOR));
    }
}
