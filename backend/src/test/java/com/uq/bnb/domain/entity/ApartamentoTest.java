package com.uq.bnb.domain.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.EstadoOperativo;
import com.uq.bnb.fixtures.TestIds;
import java.util.List;
import org.junit.jupiter.api.Test;

class ApartamentoTest {
  private Apartamento crear() {
    return Apartamento.crear(
        TestIds.next(),
        "APT-001",
        "Apartamento central",
        4,
        List.of(
            new Dormitorio("Dormitorio 1", "Cama doble"),
            new Dormitorio("Dormitorio 2", "Cama sencilla")),
        EstadoOperativo.PREPARADO);
  }

  @Test
  void creaConEstadoActivoYValoresRecibidos() {
    Apartamento apartamento = crear();

    assertEquals("APT-001", apartamento.identificacion());
    assertEquals(4, apartamento.capacidad());
    assertEquals(2, apartamento.dormitorios().size());
    assertEquals(EstadoOperativo.PREPARADO, apartamento.estadoOperativo());
    assertTrue(apartamento.activo());
  }

  @Test
  void verificaCapacidadYPermisoDeMascotas() {
    Apartamento apartamento = crear();

    assertTrue(apartamento.admiteGrupo(4, false));
    assertFalse(apartamento.admiteGrupo(5, false));
    assertFalse(apartamento.admiteGrupo(2, true));
  }

  @Test
  void transitaEntreEstadosOperativosValidos() {
    Apartamento apartamento = crear();
    apartamento.registrarOcupacion();
    assertEquals(EstadoOperativo.OCUPADO, apartamento.estadoOperativo());
    apartamento.registrarDesocupacion();
    apartamento.iniciarPreparacion();
    apartamento.marcarPreparado();
    assertEquals(EstadoOperativo.PREPARADO, apartamento.estadoOperativo());
  }

  @Test
  void rechazaTransicionesInvalidas() {
    Apartamento apartamento = crear();
    assertThrows(ReglaDominioException.class, apartamento::marcarPreparado);
  }
}
