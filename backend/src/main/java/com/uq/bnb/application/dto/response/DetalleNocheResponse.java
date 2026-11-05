package com.uq.bnb.application.dto.response;

import java.time.LocalDate;

public record DetalleNocheResponse(
    LocalDate fecha,
    String temporada,
    long tarifaAplicada,
    int ocupantesFacturables,
    long subtotal) {}
