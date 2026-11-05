package com.uq.bnb.domain.valueobject;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public record HorarioAlojamiento(LocalTime horaEntrada, LocalTime horaSalida) {
  public HorarioAlojamiento {
    Objects.requireNonNull(horaEntrada, "La hora de entrada no puede ser nula");
    Objects.requireNonNull(horaSalida, "La hora de salida no puede ser nula");
  }

  public Duration ventanaDeRecambio() {
    return Duration.between(horaSalida, horaEntrada);
  }

  public boolean permiteRecambioMismoDia(TiempoPreparacion tiempoPreparacion) {
    Objects.requireNonNull(tiempoPreparacion, "El tiempo de preparación no puede ser nulo");
    return !ventanaDeRecambio().minusHours(tiempoPreparacion.horas()).isNegative();
  }
}
