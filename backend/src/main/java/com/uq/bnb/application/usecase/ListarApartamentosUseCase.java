package com.uq.bnb.application.usecase;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import java.util.Comparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarApartamentosUseCase {
  private final ApartamentoRepository apartamentoRepository;

  public ListarApartamentosUseCase(ApartamentoRepository apartamentoRepository) {
    this.apartamentoRepository = apartamentoRepository;
  }

  @Transactional(readOnly = true)
  public Page<Apartamento> ejecutar(Pageable pageable) {
    return Paginacion.paginar(
        apartamentoRepository.obtenerTodos().stream()
            .sorted(Comparator.comparing(Apartamento::identificacion))
            .toList(),
        pageable);
  }
}
