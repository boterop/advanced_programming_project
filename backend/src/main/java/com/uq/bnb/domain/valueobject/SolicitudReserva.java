package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.entity.Titular;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public record SolicitudReserva(
    Titular titular,
    List<Ocupante> acompanantes,
    Estancia estancia,
    CanalOrigen canalOrigen,
    boolean conMascota,
    LocalTime horaEstimadaLlegada) {
  public SolicitudReserva {
    Objects.requireNonNull(titular, "El titular no puede ser nulo");
    Objects.requireNonNull(acompanantes, "Los acompañantes no pueden ser nulos");
    Objects.requireNonNull(estancia, "La estancia no puede ser nula");
    Objects.requireNonNull(canalOrigen, "El canal de origen no puede ser nulo");
    acompanantes = List.copyOf(acompanantes);
  }
}
