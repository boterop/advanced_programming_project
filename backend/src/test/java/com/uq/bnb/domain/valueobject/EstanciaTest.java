package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EstanciaTest {
  private final LocalDate inicio = LocalDate.of(2030, 6, 10);

  @Test
  void calculaNochesYFechasOcupadas() {
    Estancia estancia = new Estancia(inicio, inicio.plusDays(3));

    assertEquals(3, estancia.noches());
    assertEquals(3, estancia.nochesOcupadas().size());
    assertEquals(inicio, estancia.nochesOcupadas().getFirst());
    assertFalse(estancia.incluyeFecha(inicio.plusDays(3)));
  }

  @Test
  void detectaSolapamientoYContiguedad() {
    Estancia estancia = new Estancia(inicio, inicio.plusDays(3));
    assertTrue(estancia.seSolapaCon(new Estancia(inicio.plusDays(2), inicio.plusDays(5))));
    assertFalse(estancia.seSolapaCon(new Estancia(inicio.plusDays(3), inicio.plusDays(5))));
    assertTrue(estancia.esContiguaA(new Estancia(inicio.plusDays(3), inicio.plusDays(5))));
  }

  @Test
  void rechazaRangoSinNoches() {
    assertThrows(ReglaDominioException.class, () -> new Estancia(inicio, inicio));
    assertThrows(ReglaDominioException.class, () -> new Estancia(inicio.plusDays(1), inicio));
  }
}
