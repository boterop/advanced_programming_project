package com.uq.bnb.application.dto.response;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import java.time.LocalDate;
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
