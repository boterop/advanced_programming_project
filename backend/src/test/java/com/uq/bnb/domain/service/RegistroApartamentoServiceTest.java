package com.uq.bnb.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.fixtures.ApartamentoFixture;
import com.uq.bnb.infrastructure.persistence.inmemory.ApartamentoRepositoryInMemory;
import com.uq.bnb.infrastructure.persistence.inmemory.ReservaRepositoryInMemory;
import org.junit.jupiter.api.Test;

class RegistroApartamentoServiceTest {
  private final ApartamentoRepositoryInMemory apartamentos = new ApartamentoRepositoryInMemory();
  private final RegistroApartamentoService servicio =
      new RegistroApartamentoService(apartamentos, new ReservaRepositoryInMemory());

  @Test
  void registraUnApartamentoNuevo() {
    Apartamento apartamento = ApartamentoFixture.crear();

    servicio.registrar(apartamento);

    assertEquals(apartamento, apartamentos.buscarPorId(apartamento.identificacion()).orElseThrow());
  }

  @Test
  void rechazaUnaIdentificacionYaRegistrada() {
    Apartamento apartamento = ApartamentoFixture.crear();
    servicio.registrar(apartamento);

    assertThrows(ReglaDominioException.class, () -> servicio.registrar(apartamento));
  }
}
