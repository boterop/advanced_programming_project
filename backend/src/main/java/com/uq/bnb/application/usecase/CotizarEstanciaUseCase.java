package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.service.CotizadorEstanciaService;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.Estancia;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CotizarEstanciaUseCase {
  private final AlojamientoRepository alojamientoRepository;
  private final ApartamentoRepository apartamentoRepository;
  private final CotizadorEstanciaService cotizadorEstanciaService;

  public CotizarEstanciaUseCase(
      AlojamientoRepository alojamientoRepository,
      ApartamentoRepository apartamentoRepository,
      CotizadorEstanciaService cotizadorEstanciaService) {
    this.alojamientoRepository = alojamientoRepository;
    this.apartamentoRepository = apartamentoRepository;
    this.cotizadorEstanciaService = cotizadorEstanciaService;
  }

  @Transactional(readOnly = true)
  public Cotizacion ejecutar(
      String identificacionApartamento, Estancia estancia, List<Ocupante> ocupantes) {
    Alojamiento alojamiento =
        alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
    Apartamento apartamento =
        apartamentoRepository
            .buscarPorId(identificacionApartamento)
            .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento));
    return cotizadorEstanciaService.cotizar(alojamiento, apartamento, estancia, ocupantes);
  }
}
