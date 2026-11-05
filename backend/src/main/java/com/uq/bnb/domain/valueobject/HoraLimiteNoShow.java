package com.uq.bnb.domain.valueobject;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
public record HoraLimiteNoShow(LocalTime hora) {
    public HoraLimiteNoShow {
        Objects.requireNonNull(hora, "La hora límite de no-show no puede ser nula");
    }
    public boolean permiteDeclarar(LocalDate fechaEntrada, LocalDateTime ahora) {
        Objects.requireNonNull(fechaEntrada, "La fecha de entrada no puede ser nula");
        Objects.requireNonNull(ahora, "El momento actual no puede ser nulo");
        return !ahora.isBefore(fechaEntrada.atTime(hora));
    }
}
