package com.uq.bnb.application.dto.response;

import java.time.Instant;
import java.util.List;

public record TokenResponse(
        String token,
        String tipo,
        Instant expiraEn,
        List<String> roles
) {
}
