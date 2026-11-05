package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.service.RegistroApartamentoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CambiarCapacidadApartamentoUseCase {
  private final ApartamentoRepository apartamentoRepository;
  private final RegistroApartamentoService registroApartamentoService;

  public CambiarCapacidadApartamentoUseCase(
      ApartamentoRepository apartamentoRepository,
      RegistroApartamentoService registroApartamentoService) {
    this.apartamentoRepository = apartamentoRepository;
    this.registroApartamentoService = registroApartamentoService;
  }

  @Transactional
  public Apartamento ejecutar(String identificacion, int nuevaCapacidad) {
    Apartamento apartamento =
        apartamentoRepository
            .buscarPorId(identificacion)
            .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacion));
    registroApartamentoService.cambiarCapacidad(apartamento, nuevaCapacidad);
    apartamentoRepository.guardar(apartamento);
    return apartamento;
  }
}
