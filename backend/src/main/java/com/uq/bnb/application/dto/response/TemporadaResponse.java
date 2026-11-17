package com.uq.bnb.application.dto.response;

import java.time.LocalDate;
import java.util.UUID;

public record TemporadaResponse(
    UUID id, String nombre, LocalDate fechaInicio, LocalDate fechaFin, boolean esBase) {}
