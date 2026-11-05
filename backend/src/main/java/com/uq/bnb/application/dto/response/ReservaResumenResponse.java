package com.uq.bnb.application.dto.response;

import com.uq.bnb.domain.valueobject.EstadoReserva;

import java.time.LocalDate;

/** Cada elemento del listado paginado de GET /api/reservas. */
public record ReservaResumenResponse(
        String codigo,
        EstadoReserva estado,
        String identificacionApartamento,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        String nombreTitular,
        int totalOcupantes,
        long valorTotal

) {
}
