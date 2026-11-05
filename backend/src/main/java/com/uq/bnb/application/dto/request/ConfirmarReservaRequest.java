package com.uq.bnb.application.dto.request;
import com.uq.bnb.domain.valueobject.MedioPago;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
public record ConfirmarReservaRequest(
        LocalTime horaEstimadaLlegada,
        MedioPago medioPago,
        @Size(max = 60, message = "La referencia del pago no puede superar 60 caracteres")
        String referencia
) {
}
