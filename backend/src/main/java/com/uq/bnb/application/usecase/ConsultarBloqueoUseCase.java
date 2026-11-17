package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.BloqueoNoEncontradoException;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultarBloqueoUseCase {
  private final ApartamentoRepository apartamentoRepository;

  public ConsultarBloqueoUseCase(ApartamentoRepository apartamentoRepository) {
    this.apartamentoRepository = apartamentoRepository;
  }

  @Transactional(readOnly = true)
  public Bloqueo ejecutar(String apartamentoId, UUID idBloqueo) {
    return apartamentoRepository
        .buscarPorId(apartamentoId)
        .orElseThrow(() -> new ApartamentoNoEncontradoException(apartamentoId))
        .bloqueo(new com.uq.bnb.domain.valueobject.ID(idBloqueo.toString()))
        .orElseThrow(() -> new BloqueoNoEncontradoException(apartamentoId, idBloqueo));
  }
}
