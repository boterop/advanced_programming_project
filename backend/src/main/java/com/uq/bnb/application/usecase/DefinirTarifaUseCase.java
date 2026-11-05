package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.TemporadaNoEncontradaException;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Tarifa;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.valueobject.Dinero;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DefinirTarifaUseCase {
  private final ApartamentoRepository apartamentoRepository;
  private final AlojamientoRepository alojamientoRepository;
  private final IDProvider idProvider;

  public DefinirTarifaUseCase(
      ApartamentoRepository apartamentoRepository,
      AlojamientoRepository alojamientoRepository,
      IDProvider idProvider) {
    this.apartamentoRepository = apartamentoRepository;
    this.alojamientoRepository = alojamientoRepository;
    this.idProvider = idProvider;
  }

  @Transactional
  public Tarifa ejecutar(
      String identificacionApartamento,
      UUID temporadaId,
      Dinero valorPorNoche,
      LocalDate vigenteDesde) {
    Apartamento apartamento =
        apartamentoRepository
            .buscarPorId(identificacionApartamento)
            .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento));
    alojamientoRepository
        .obtener()
        .orElseThrow(AlojamientoNoEncontradoException::new)
        .temporada(new com.uq.bnb.domain.valueobject.ID(temporadaId.toString()))
        .orElseThrow(() -> new TemporadaNoEncontradaException(temporadaId));
    Tarifa nueva =
        apartamento.definirTarifa(
            idProvider.obtener(),
            new com.uq.bnb.domain.valueobject.ID(temporadaId.toString()),
            valorPorNoche,
            vigenteDesde);
    apartamentoRepository.guardar(apartamento);
    return nueva;
  }
}
