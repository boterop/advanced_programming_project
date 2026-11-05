package com.uq.bnb.domain.valueobject;
import com.uq.bnb.domain.exception.ReglaDominioException;
import java.time.LocalDateTime;
import java.util.Objects;
public record PlazoConfirmacion(int horas) {
    public PlazoConfirmacion {
        if (horas <= 0) {
            throw new ReglaDominioException("El plazo de confirmación debe ser mayor a cero horas");
        }
    }
    public boolean haVencido(LocalDateTime creadaEn, LocalDateTime ahora) {
        Objects.requireNonNull(creadaEn, "El momento de creación no puede ser nulo");
        Objects.requireNonNull(ahora, "El momento actual no puede ser nulo");
        return ahora.isAfter(creadaEn.plusHours(horas));
    }
}
