package com.uq.bnb.application.security;

import java.time.Instant;

public record TokenEmitido(String token, Instant expiraEn) {
}
