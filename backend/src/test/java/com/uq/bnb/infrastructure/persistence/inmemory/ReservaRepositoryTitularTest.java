package com.uq.bnb.infrastructure.persistence.inmemory;

import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.fixtures.ReservaFixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReservaRepositoryTitularTest {
    @Test
    void buscaLasReservasActivasPorElDocumentoDelTitular() {
        ReservaRepositoryInMemory repository = new ReservaRepositoryInMemory();
        Reserva reserva = ReservaFixture.crear();
        repository.guardar(reserva);

        assertEquals(1, repository.buscarActivasPorTitular(reserva.titular().documento()).size());
        assertTrue(repository.buscarActivasPorTitular("999999999").isEmpty());
    }
}
