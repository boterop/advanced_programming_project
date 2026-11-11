package com.uq.bnb.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.fixtures.ApartamentoFixture;
import com.uq.bnb.fixtures.BloqueoFixture;
import com.uq.bnb.fixtures.ReservaFixture;
import com.uq.bnb.infrastructure.persistence.inmemory.ReservaRepositoryInMemory;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class GestionBloqueoServiceTest {
  private final ReservaRepositoryInMemory reservas = new ReservaRepositoryInMemory();
  private final GestionBloqueoService servicio = new GestionBloqueoService(reservas);

  @Test
  void agregaUnBloqueoCuandoNoHayReservasActivasEnElPeriodo() {
    Apartamento apartamento = ApartamentoFixture.crear();
    Bloqueo bloqueo = BloqueoFixture.crear();

    servicio.registrarBloqueo(apartamento, bloqueo);

    assertEquals(1, apartamento.bloqueos().size());
  }

  @Test
  void rechazaUnBloqueoQueSeCruzaConUnaReservaActiva() {
    Apartamento apartamento = ApartamentoFixture.crear();
    reservas.guardar(ReservaFixture.crear());
    Bloqueo bloqueo =
        Bloqueo.crear(
            com.uq.bnb.fixtures.TestIds.next(),
            new Estancia(LocalDate.of(2030, 6, 10), LocalDate.of(2030, 6, 12)),
            "Mantenimiento");

    assertThrows(
        ReglaDominioException.class, () -> servicio.registrarBloqueo(apartamento, bloqueo));
  }
}
