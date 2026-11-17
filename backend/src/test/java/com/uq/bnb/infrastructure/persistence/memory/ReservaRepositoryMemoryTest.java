package com.uq.bnb.infrastructure.persistence.inmemory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.fixtures.ReservaFixture;
import org.junit.jupiter.api.Test;

class ReservaRepositoryMemoryTest {
  @Test
  void guardaYBuscaPorCodigo() {
    ReservaRepositoryInMemory repository = new ReservaRepositoryInMemory();
    Reserva reserva = ReservaFixture.crear();

    repository.guardar(reserva);

    assertEquals(reserva, repository.obtenerPorCodigo(reserva.codigo()).orElseThrow());
  }

  @Test
  void listaReservasActivasDelApartamento() {
    ReservaRepositoryInMemory repository = new ReservaRepositoryInMemory();
    Reserva reserva = ReservaFixture.crear();
    repository.guardar(reserva);

    assertEquals(1, repository.buscarActivasPorApartamento(reserva.apartamentoId()).size());
    assertTrue(repository.buscarPorEstado(EstadoReserva.CANCELADA).isEmpty());
  }
}
