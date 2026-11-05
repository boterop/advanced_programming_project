package com.uq.bnb.application.dto.request;

import com.uq.bnb.domain.valueobject.CanalOrigen;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CrearReservaRequest(
    @NotBlank(message = "La identificación del apartamento es obligatoria")
        String identificacionApartamento,
    @NotNull(message = "La fecha de entrada es obligatoria") LocalDate fechaEntrada,
    @NotNull(message = "La fecha de salida es obligatoria") LocalDate fechaSalida,
    @NotNull(message = "El canal de origen es obligatorio") CanalOrigen canalOrigen,
    LocalTime horaEstimadaLlegada,
    Boolean conMascota,
    @NotNull(message = "La reserva debe tener titular") @Valid OcupanteRequest titular,
    @NotNull(message = "La lista de acompañantes es obligatoria (puede ir vacía)") @Valid
        List<@NotNull(message = "Un acompañante no puede ser nulo") OcupanteRequest> ocupantes) {}
