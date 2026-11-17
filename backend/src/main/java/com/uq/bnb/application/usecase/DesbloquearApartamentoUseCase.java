package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.BloqueoNoEncontradoException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.valueobject.ID;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DesbloquearApartamentoUseCase {
  private final ApartamentoRepository apartamentoRepository;

  public DesbloquearApartamentoUseCase(ApartamentoRepository apartamentoRepository) {
    this.apartamentoRepository = apartamentoRepository;
  }

  @Transactional
  public void ejecutar(String apartamentoId, UUID idBloqueo) {
    Apartamento apartamento =
        apartamentoRepository
            .buscarPorId(apartamentoId)
            .orElseThrow(() -> new ApartamentoNoEncontradoException(apartamentoId));
    if (!apartamento.eliminarBloqueo(new ID(idBloqueo.toString()))) {
      throw new BloqueoNoEncontradoException(apartamentoId, idBloqueo);
    }
    apartamentoRepository.guardar(apartamento);
  }
}
