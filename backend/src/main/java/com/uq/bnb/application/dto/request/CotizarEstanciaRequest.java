package com.uq.bnb.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record CotizarEstanciaRequest(
    @NotBlank(message = "El ID del apartamento es obligatorio")
        String apartamentoId,
    @NotNull(message = "La fecha de entrada es obligatoria") LocalDate fechaEntrada,
    @NotNull(message = "La fecha de salida es obligatoria") LocalDate fechaSalida,
    @NotEmpty(message = "Debe indicar al menos un ocupante") @Valid
        List<@NotNull(message = "Un ocupante no puede ser nulo") OcupanteRequest> ocupantes) {}
