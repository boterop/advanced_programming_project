package com.uq.bnb.application.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
public record RegistrarTemporadaRequest(
        @NotBlank(message = "El nombre de la temporada es obligatorio")
        String nombre,
        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,
        @NotNull(message = "La fecha de fin es obligatoria")
        LocalDate fechaFin
) {
}
