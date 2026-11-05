package com.uq.bnb.application.dto.response;
import java.util.List;
public record FolioResponse(
        String codigoReserva,
        List<CargoResponse> cargos,
        List<PagoResponse> pagos,
        long totalCargos,
        long totalPagos,
        long saldoPendiente,
        String moneda
) {
}
