package com.uq.bnb.application.dto.request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
public record DefinirPoliticaCancelacionRequest(
        @NotEmpty(message = "La política debe tener tramos")
        @Valid
        List<@NotNull(message = "Un tramo no puede ser nulo") TramoRequest> tramos,
        @NotNull(message = "La fecha desde la que rige la política es obligatoria")
        LocalDate vigenteDesde
) {
}
