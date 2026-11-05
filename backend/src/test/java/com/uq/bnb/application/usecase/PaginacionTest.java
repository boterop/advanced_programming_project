package com.uq.bnb.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

class PaginacionTest {
  @Test
  void devuelveElContenidoYTotalDeLaPaginaSolicitada() {
    List<Integer> elementos = java.util.stream.IntStream.rangeClosed(1, 25).boxed().toList();

    Page<Integer> pagina = Paginacion.paginar(elementos, PageRequest.of(2, 10));

    assertEquals(List.of(21, 22, 23, 24, 25), pagina.getContent());
    assertEquals(25, pagina.getTotalElements());
    assertEquals(3, pagina.getTotalPages());
  }

  @Test
  void devuelvePaginaVaciaAlSolicitarUnaPaginaFueraDelRango() {
    Page<Integer> pagina = Paginacion.paginar(List.of(1, 2, 3), PageRequest.of(3, 10));

    assertTrue(pagina.isEmpty());
    assertEquals(3, pagina.getTotalElements());
  }
}
