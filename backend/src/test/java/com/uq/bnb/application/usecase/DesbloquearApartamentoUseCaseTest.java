package com.uq.bnb.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.uq.bnb.application.exception.BloqueoNoEncontradoException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.fixtures.ApartamentoFixture;
import com.uq.bnb.fixtures.TestIds;
import com.uq.bnb.infrastructure.persistence.inmemory.ApartamentoRepositoryInMemory;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DesbloquearApartamentoUseCaseTest {
  private final ApartamentoRepositoryInMemory apartamentos = new ApartamentoRepositoryInMemory();
  private final DesbloquearApartamentoUseCase useCase =
      new DesbloquearApartamentoUseCase(apartamentos);

  @Test
  void eliminaElBloqueoDelApartamento() {
    Apartamento apartamento = ApartamentoFixture.crear();
    Bloqueo bloqueo =
        Bloqueo.crear(
            TestIds.next(),
            new Estancia(LocalDate.now().plusDays(2), LocalDate.now().plusDays(4)),
            "Mantenimiento");
    apartamento.agregarBloqueo(bloqueo);
    apartamentos.guardar(apartamento);

    useCase.ejecutar(apartamento.identificacion(), java.util.UUID.fromString(bloqueo.id().valor()));

    assertEquals(
        0, apartamentos.buscarPorId(apartamento.identificacion()).orElseThrow().bloqueos().size());
  }

  @Test
  void rechazaUnBloqueoQueNoExiste() {
    Apartamento apartamento = ApartamentoFixture.crear();
    apartamentos.guardar(apartamento);

    assertThrows(
        BloqueoNoEncontradoException.class,
        () -> useCase.ejecutar(apartamento.identificacion(), java.util.UUID.randomUUID()));
  }
}
