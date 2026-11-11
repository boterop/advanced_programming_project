package com.uq.bnb.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.MedioPago;
import com.uq.bnb.domain.valueobject.PlazoConfirmacion;
import com.uq.bnb.fixtures.ReservaFixture;
import com.uq.bnb.fixtures.TestIds;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class ReservaTest {
  @Test
  void inicializaPendienteConCargosDeHospedaje() {
    Reserva reserva = ReservaFixture.crear();

    assertEquals(EstadoReserva.PENDIENTE, reserva.estado());
    assertEquals(1, reserva.folio().cargos().size());
    assertEquals(1, reserva.cuposNecesarios());
  }

  @Test
  void permiteConfirmarUnaReservaPendiente() {
    Reserva reserva = ReservaFixture.crear();

    reserva.confirmar();

    assertEquals(EstadoReserva.CONFIRMADA, reserva.estado());
  }

  @Test
  void noPermiteConfirmarUnaReservaMasDeUnaVez() {
    Reserva reserva = ReservaFixture.crear();
    reserva.confirmar();

    assertThrows(ReglaDominioException.class, reserva::confirmar);
  }

  @Test
  void identificaReservasPendientesConPlazoDeConfirmacionVencido() {
    Reserva reserva = ReservaFixture.crear();

    assertTrue(
        reserva.haVencidoPlazoConfirmacion(
            new PlazoConfirmacion(24), LocalDateTime.of(2030, 1, 3, 12, 1)));
    assertFalse(
        reserva.haVencidoPlazoConfirmacion(
            new PlazoConfirmacion(24), LocalDateTime.of(2030, 1, 2, 12, 0)));
  }

  @Test
  void noPermiteFinalizarUnaReservaConSaldoPendiente() {
    Reserva reserva = ReservaFixture.crear();
    reserva.confirmar();
    reserva.realizarCheckIn();

    assertThrows(ReglaDominioException.class, reserva::realizarCheckOut);
  }

  @Test
  void finalizaUnaReservaCuandoElFolioEstaPagado() {
    Reserva reserva = ReservaFixture.crear();
    reserva.confirmar();
    reserva.realizarCheckIn();
    reserva
        .folio()
        .registrarPago(TestIds.next(), Dinero.de(100_000), MedioPago.EFECTIVO, "pago de prueba");

    reserva.realizarCheckOut();

    assertEquals(EstadoReserva.FINALIZADA, reserva.estado());
  }
}
