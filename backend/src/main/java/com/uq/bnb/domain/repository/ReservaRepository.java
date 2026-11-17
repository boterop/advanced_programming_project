package com.uq.bnb.domain.repository;

import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.Estancia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservaRepository {
  List<Reserva> buscarActivasPorApartamento(String apartamentoId, Estancia estancia);

  List<Reserva> buscarActivasPorApartamento(String apartamentoId);

  Optional<Reserva> obtenerPorCodigo(CodigoReserva codigo);

  List<Reserva> buscarPorEstado(EstadoReserva estado);

  List<Reserva> buscar(
      EstadoReserva estado, String apartamentoId, LocalDate desde, LocalDate hasta);

  void guardar(Reserva reserva);
}
