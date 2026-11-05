package com.uq.bnb.domain.valueobject;
import java.time.LocalDateTime;
import java.util.Objects;
public record Cancelacion(String motivo, LocalDateTime fecha, Dinero retenido, Dinero devuelto) {
    public Cancelacion {
        Objects.requireNonNull(motivo, "El motivo de la cancelación no puede ser nulo");
        Objects.requireNonNull(fecha, "La fecha de la cancelación no puede ser nula");
        Objects.requireNonNull(retenido, "El valor retenido no puede ser nulo");
        Objects.requireNonNull(devuelto, "El valor devuelto no puede ser nulo");
    }
}
