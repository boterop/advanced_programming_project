package com.uq.bnb.application.dto.response;
import java.time.LocalDate;
import java.util.List;
public record CotizacionResponse(
        String identificacionApartamento,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        int noches,
        int ocupantesFacturables,
        List<DetalleNocheResponse> desglose,
        long valorTotal,
        String moneda
) {
}
