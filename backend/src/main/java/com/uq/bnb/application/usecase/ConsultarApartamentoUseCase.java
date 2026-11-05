package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultarApartamentoUseCase {
  private final ApartamentoRepository apartamentoRepository;

  public ConsultarApartamentoUseCase(ApartamentoRepository apartamentoRepository) {
    this.apartamentoRepository = apartamentoRepository;
  }

  @Transactional(readOnly = true)
  public Apartamento ejecutar(String identificacion) {
    return apartamentoRepository
        .buscarPorId(identificacion)
        .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacion));
  }
}
