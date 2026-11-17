package com.uq.bnb.application.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record BloqueoResponse(
    UUID id,
    String apartamentoId,
    LocalDate fechaInicio,
    LocalDate fechaFin,
    String motivo) {}
