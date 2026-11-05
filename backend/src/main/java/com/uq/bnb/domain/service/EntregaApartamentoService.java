package com.uq.bnb.domain.service;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import java.time.LocalDate;
import java.util.Objects;

public class EntregaApartamentoService {
  public void autorizarEntrega(Reserva reserva, Apartamento apartamento, LocalDate hoy) {
    Objects.requireNonNull(reserva, "La reserva no puede ser nula");
    Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
    Objects.requireNonNull(hoy, "La fecha actual no puede ser nula");
    if (!reserva.apartamentoId().equals(apartamento.identificacion())) {
      throw new ReglaDominioException(
          "La reserva "
              + reserva.codigo().valor()
              + " no corresponde al apartamento "
              + apartamento.identificacion());
    }
    if (reserva.estado() != EstadoReserva.CONFIRMADA) {
      throw new ReglaDominioException(
          "RN-10: el registro de llegada solo se hace sobre una reserva CONFIRMADA (estado actual: "
              + reserva.estado()
              + ")");
    }
    if (hoy.isBefore(reserva.estancia().entrada())) {
      throw new ReglaDominioException(
          "RN-10: no se permite el registro antes de la fecha de entrada ("
              + reserva.estancia().entrada()
              + ")");
    }
    if (!hoy.isBefore(reserva.estancia().salida())) {
      throw new ReglaDominioException(
          "RN-10: la estancia ya terminó el "
              + reserva.estancia().salida()
              + "; no se puede registrar la llegada");
    }
    if (!apartamento.estadoOperativo().permiteRegistro()) {
      throw new ReglaDominioException(
          "RN-11: el apartamento "
              + apartamento.identificacion()
              + " debe estar PREPARADO para recibir a un grupo (estado actual: "
              + apartamento.estadoOperativo()
              + ")");
    }
  }
}
