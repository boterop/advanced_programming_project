package com.uq.bnb.infrastructure.security;

import com.uq.bnb.application.port.EmisorToken;
import com.uq.bnb.application.security.TokenEmitido;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

// Construye y firma el JWT del SGA.
// El token va firmado, no cifrado.
@Component
public class JwtTokenEmisor implements EmisorToken {
    private final JwtEncoder encoder;
    private final String emisor;
    private final long expiracionMinutos;

    public JwtTokenEmisor(
            JwtEncoder encoder,
            @Value("${sga.jwt.emisor}") String emisor,
            @Value("${sga.jwt.expiracion-minutos}") long expiracionMinutos) {
        this.encoder = encoder;
        this.emisor = emisor;
        this.expiracionMinutos = expiracionMinutos;
    }

    @Override
    public TokenEmitido emitir(String correo, String documento, List<String> roles) {
        // Los claims de fecha viajan en segundos: se trunca para que expiraEn coincida con el token.
        Instant emitidoEn = Instant.now().truncatedTo(ChronoUnit.SECONDS);
        Instant expiraEn = emitidoEn.plus(expiracionMinutos, ChronoUnit.MINUTES);

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer(emisor)
                        .issuedAt(emitidoEn)
                        .expiresAt(expiraEn)
                        .subject(correo)
                        .claim("roles", roles)
                        .claim("documento", documento)
                        .build();

        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        return new TokenEmitido(token, expiraEn);
    }
}
