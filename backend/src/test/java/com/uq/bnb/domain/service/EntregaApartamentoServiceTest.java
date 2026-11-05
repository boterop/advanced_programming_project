package com.uq.bnb.domain.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.fixtures.ApartamentoFixture;
import com.uq.bnb.fixtures.ReservaFixture;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class EntregaApartamentoServiceTest {
  private final EntregaApartamentoService servicio = new EntregaApartamentoService();

  @Test
  void autorizaLaEntregaDeUnaReservaConfirmadaEnLaFechaDeEntrada() {
    Apartamento apartamento = ApartamentoFixture.crear();
    Reserva reserva = ReservaFixture.crear();
    reserva.confirmar();

    assertDoesNotThrow(
        () -> servicio.autorizarEntrega(reserva, apartamento, LocalDate.of(2030, 6, 10)));
  }

  @Test
  void rechazaLaEntregaAntesDeLaFechaDeEntrada() {
    Apartamento apartamento = ApartamentoFixture.crear();
    Reserva reserva = ReservaFixture.crear();
    reserva.confirmar();

    assertThrows(
        ReglaDominioException.class,
        () -> servicio.autorizarEntrega(reserva, apartamento, LocalDate.of(2030, 6, 9)));
  }

  @Test
  void rechazaLaEntregaSiLaReservaNoEstaConfirmada() {
    Apartamento apartamento = ApartamentoFixture.crear();
    Reserva reserva = ReservaFixture.crear();

    assertThrows(
        ReglaDominioException.class,
        () -> servicio.autorizarEntrega(reserva, apartamento, LocalDate.of(2030, 6, 10)));
  }
}
