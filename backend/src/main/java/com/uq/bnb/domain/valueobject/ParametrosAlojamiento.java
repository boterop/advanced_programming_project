package com.uq.bnb.domain.valueobject;
import java.util.Objects;
public record ParametrosAlojamiento(
        UmbralEdadFacturable umbralEdadFacturable,
        HorarioAlojamiento horario,
        TiempoPreparacion tiempoPreparacion,
        PlazoConfirmacion plazoConfirmacion,
        HoraLimiteNoShow horaLimiteNoShow,
        PoliticaAnticipo politicaAnticipo) {
    public ParametrosAlojamiento {
        Objects.requireNonNull(umbralEdadFacturable, "El umbral de edad facturable no puede ser nulo");
        Objects.requireNonNull(horario, "El horario no puede ser nulo");
        Objects.requireNonNull(tiempoPreparacion, "El tiempo de preparación no puede ser nulo");
        Objects.requireNonNull(plazoConfirmacion, "El plazo de confirmación no puede ser nulo");
        Objects.requireNonNull(horaLimiteNoShow, "La hora límite de no-show no puede ser nula");
        Objects.requireNonNull(politicaAnticipo, "La política de anticipo no puede ser nula");
    }
}
