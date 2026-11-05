package com.uq.bnb.application.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
public record BuscarDisponibilidadRequest(
        @NotNull(message = "La fecha de entrada es obligatoria")
        LocalDate fechaEntrada,
        @NotNull(message = "La fecha de salida es obligatoria")
        LocalDate fechaSalida,
        @NotNull(message = "El número de adultos es obligatorio")
        @Min(value = 1, message = "Debe haber al menos un adulto (el titular)")
        Integer adultos,
        @Min(value = 0, message = "El número de niños no puede ser negativo")
        Integer ninos,
        Boolean conMascota
) {
}
