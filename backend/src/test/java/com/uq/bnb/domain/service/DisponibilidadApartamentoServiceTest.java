package com.uq.bnb.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.EstadoOperativo;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.HoraLimiteNoShow;
import com.uq.bnb.domain.valueobject.HorarioAlojamiento;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.domain.valueobject.PlazoConfirmacion;
import com.uq.bnb.domain.valueobject.PoliticaAnticipo;
import com.uq.bnb.domain.valueobject.TiempoPreparacion;
import com.uq.bnb.domain.valueobject.UmbralEdadFacturable;
import com.uq.bnb.fixtures.ApartamentoFixture;
import com.uq.bnb.fixtures.ReservaFixture;
import com.uq.bnb.fixtures.TestIds;
import com.uq.bnb.infrastructure.persistence.inmemory.ApartamentoRepositoryInMemory;
import com.uq.bnb.infrastructure.persistence.inmemory.ReservaRepositoryInMemory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class DisponibilidadApartamentoServiceTest {
  private final ApartamentoRepositoryInMemory apartamentos = new ApartamentoRepositoryInMemory();
  private final ReservaRepositoryInMemory reservas = new ReservaRepositoryInMemory();
  private final DisponibilidadApartamentoService servicio =
      new DisponibilidadApartamentoService(apartamentos, reservas);
  private final Estancia estancia =
      new Estancia(LocalDate.of(2030, 6, 10), LocalDate.of(2030, 6, 13));
  private final ParametrosAlojamiento parametros =
      new ParametrosAlojamiento(
          new UmbralEdadFacturable(18),
          new HorarioAlojamiento(LocalTime.of(15, 0), LocalTime.of(11, 0)),
          new TiempoPreparacion(0),
          new PlazoConfirmacion(24),
          new HoraLimiteNoShow(LocalTime.NOON),
          PoliticaAnticipo.noExige());

  @Test
  void listaApartamentosDisponiblesOrdenadosPorIdentificacion() {
    Apartamento segundo =
        Apartamento.crear(
            TestIds.next(),
            "APT-002",
            "Segundo",
            4,
            List.of(new Dormitorio("Dormitorio", "Cama doble")),
            EstadoOperativo.PREPARADO);
    Apartamento primero = ApartamentoFixture.crear();
    apartamentos.guardar(segundo);
    apartamentos.guardar(primero);

    assertEquals(
        List.of(primero, segundo), servicio.buscarDisponibles(estancia, 2, false, parametros));
  }

  @Test
  void excluyeApartamentosConBloqueosEnElPeriodo() {
    Apartamento apartamento = ApartamentoFixture.crear();
    apartamento.agregarBloqueo(Bloqueo.crear(TestIds.next(), estancia, "Mantenimiento"));
    apartamentos.guardar(apartamento);

    assertTrue(servicio.buscarDisponibles(estancia, 2, false, parametros).isEmpty());
  }

  @Test
  void excluyeReservasActivasQueSeSolapanConLaEstancia() {
    Apartamento apartamento = ApartamentoFixture.crear();
    apartamentos.guardar(apartamento);
    reservas.guardar(ReservaFixture.crear());

    assertTrue(servicio.buscarDisponibles(estancia, 2, false, parametros).isEmpty());
  }

  @Test
  void excluyeGruposQueExcedenLaCapacidad() {
    Apartamento apartamento = ApartamentoFixture.crear();
    apartamentos.guardar(apartamento);

    assertFalse(servicio.buscarDisponibles(estancia, 5, false, parametros).contains(apartamento));
  }
}
