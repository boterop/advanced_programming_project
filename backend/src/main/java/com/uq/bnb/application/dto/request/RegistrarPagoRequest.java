package com.uq.bnb.application.dto.request;

import com.uq.bnb.domain.valueobject.MedioPago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record RegistrarPagoRequest(
    @NotNull(message = "El medio de pago es obligatorio") MedioPago medioPago,
    @NotNull(message = "El valor del pago es obligatorio")
        @Positive(message = "El valor del pago debe ser mayor a cero")
        BigDecimal valor,
    @Size(max = 60, message = "La referencia del pago no puede superar 60 caracteres")
        String referencia) {}
