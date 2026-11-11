package com.uq.bnb.fixtures;

import com.uq.bnb.application.port.EmisorToken;
import com.uq.bnb.application.security.TokenEmitido;

import java.time.Instant;
import java.util.List;

public class FakeEmisorToken implements EmisorToken {
    public static final Instant EXPIRA_EN = Instant.parse("2030-01-01T00:30:00Z");

    private String correo;
    private String documento;
    private List<String> roles;

    @Override
    public TokenEmitido emitir(String correo, String documento, List<String> roles) {
        this.correo = correo;
        this.documento = documento;
        this.roles = roles;
        return new TokenEmitido("token-de-prueba", EXPIRA_EN);
    }

    public String correo() {
        return correo;
    }

    public String documento() {
        return documento;
    }

    public List<String> roles() {
        return roles;
    }
}
