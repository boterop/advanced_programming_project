package com.uq.bnb.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.fixtures.ReservaFixture;
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
}
