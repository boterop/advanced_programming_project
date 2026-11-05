package com.uq.bnb.application.dto.response;
import com.uq.bnb.domain.valueobject.MedioPago;
import java.time.LocalDate;
import java.util.UUID;
public record PagoResponse(UUID id, MedioPago medio, LocalDate fecha, long valor) {
}
