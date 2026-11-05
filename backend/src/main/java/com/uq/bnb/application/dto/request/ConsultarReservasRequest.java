package com.uq.bnb.application.dto.request;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import jakarta.validation.constraints.AssertTrue;
import java.time.LocalDate;
public record ConsultarReservasRequest(
        EstadoReserva estado,
        String identificacionApartamento,
        LocalDate desde,
        LocalDate hasta
) {
    @AssertTrue(message = "La fecha 'hasta' debe ser posterior a la fecha 'desde'")
    public boolean isRangoValido() {
        return desde == null || hasta == null || hasta.isAfter(desde);
    }
}
