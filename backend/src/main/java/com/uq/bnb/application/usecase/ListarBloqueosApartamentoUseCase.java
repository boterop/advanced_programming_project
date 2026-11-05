package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import java.util.Comparator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ListarBloqueosApartamentoUseCase {
  private final ApartamentoRepository apartamentoRepository;

  public ListarBloqueosApartamentoUseCase(ApartamentoRepository apartamentoRepository) {
    this.apartamentoRepository = apartamentoRepository;
  }

  @Transactional(readOnly = true)
  public Page<Bloqueo> ejecutar(String identificacionApartamento, Pageable pageable) {
    return Paginacion.paginar(
        apartamentoRepository
            .buscarPorId(identificacionApartamento)
            .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento))
            .bloqueos()
            .stream()
            .sorted(Comparator.comparing(b -> b.rango().entrada()))
            .toList(),
        pageable);
  }
}
