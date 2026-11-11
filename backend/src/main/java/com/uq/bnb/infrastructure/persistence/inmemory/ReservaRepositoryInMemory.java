package com.uq.bnb.infrastructure.persistence.inmemory;

import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("inmemory")
public class ReservaRepositoryInMemory implements ReservaRepository {
  private final Map<ID, Reserva> tablaReservas = new HashMap<>();

  @Override
  public List<Reserva> buscarActivasPorApartamento(String apartamentoId, Estancia estancia) {
    return tablaReservas.values().stream()
        .filter(reserva -> reserva.apartamentoId().equals(apartamentoId))
        .filter(reserva -> reserva.estado() != null && reserva.estado().esActiva())
        .filter(reserva -> reserva.estancia().seSolapaCon(estancia))
        .toList();
  }

  @Override
  public List<Reserva> buscarActivasPorApartamento(String apartamentoId) {
    return tablaReservas.values().stream()
        .filter(reserva -> reserva.apartamentoId().equals(apartamentoId))
        .filter(reserva -> reserva.estado() != null && reserva.estado().esActiva())
        .toList();
  }

  @Override
  public List<Reserva> buscarActivasPorTitular(String documento) {
    return tablaReservas.values().stream()
            .filter(reserva -> reserva.titular().documento().equals(documento))
            .filter(reserva -> reserva.estado() != null && reserva.estado().esActiva())
            .toList();
  }

  @Override
  public Optional<Reserva> obtenerPorCodigo(CodigoReserva codigo) {
    return tablaReservas.values().stream().filter(r -> r.codigo().equals(codigo)).findFirst();
  }

  @Override
  public List<Reserva> buscarPorEstado(EstadoReserva estado) {
    return tablaReservas.values().stream().filter(reserva -> reserva.estado() == estado).toList();
  }

  @Override
  public List<Reserva> buscar(
      EstadoReserva estado, String apartamentoId, LocalDate desde, LocalDate hasta) {
    return tablaReservas.values().stream()
        .filter(reserva -> estado == null || reserva.estado() == estado)
        .filter(reserva -> apartamentoId == null || reserva.apartamentoId().equals(apartamentoId))
        .filter(reserva -> desde == null || reserva.estancia().salida().isAfter(desde))
        .filter(reserva -> hasta == null || reserva.estancia().entrada().isBefore(hasta))
        .sorted(Comparator.comparing(Reserva::fechaCreacion).reversed())
        .toList();
  }

  @Override
  public void guardar(Reserva reserva) {
    tablaReservas.put(reserva.id(), reserva);
  }
}
