package com.uq.bnb.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record BloquearApartamentoRequest(
        @NotNull LocalDate fechaInicio,
        @NotNull LocalDate fechaFin,
        @NotBlank String motivo
) {
}
