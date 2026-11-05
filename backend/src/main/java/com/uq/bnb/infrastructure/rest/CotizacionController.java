package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.application.dto.request.CotizarEstanciaRequest;
import com.uq.bnb.application.dto.response.CotizacionResponse;
import com.uq.bnb.application.usecase.CotizarEstanciaUseCase;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.infrastructure.rest.mapper.CotizacionMapper;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cotizaciones")
public class CotizacionController {
  private final CotizarEstanciaUseCase cotizarEstanciaUseCase;
  private final CotizacionMapper mapper;

  public CotizacionController(
      CotizarEstanciaUseCase cotizarEstanciaUseCase, CotizacionMapper mapper) {
    this.cotizarEstanciaUseCase = cotizarEstanciaUseCase;
    this.mapper = mapper;
  }

  @PostMapping
  public CotizacionResponse cotizar(@Valid @RequestBody CotizarEstanciaRequest request) {
    Estancia estancia = mapper.toEstancia(request);
    Cotizacion cotizacion =
        cotizarEstanciaUseCase.ejecutar(
            request.identificacionApartamento(), estancia, mapper.toOcupantes(request.ocupantes()));
    return mapper.toResponse(request.identificacionApartamento(), estancia, cotizacion);
  }
}
