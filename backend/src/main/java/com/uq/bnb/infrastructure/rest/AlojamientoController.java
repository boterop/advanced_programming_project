package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.application.dto.request.DefinirPoliticaCancelacionRequest;
import com.uq.bnb.application.dto.request.RegistrarTemporadaRequest;
import com.uq.bnb.application.dto.response.AlojamientoResponse;
import com.uq.bnb.application.dto.response.PoliticaCancelacionResponse;
import com.uq.bnb.application.dto.response.TemporadaResponse;
import com.uq.bnb.application.usecase.ConsultarAlojamientoUseCase;
import com.uq.bnb.application.usecase.ConsultarPoliticaCancelacionUseCase;
import com.uq.bnb.application.usecase.ConsultarTemporadaUseCase;
import com.uq.bnb.application.usecase.DefinirPoliticaCancelacionUseCase;
import com.uq.bnb.application.usecase.ListarPoliticasCancelacionUseCase;
import com.uq.bnb.application.usecase.RegistrarTemporadaUseCase;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.entity.Temporada;
import com.uq.bnb.infrastructure.rest.mapper.AlojamientoMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/alojamiento")
public class AlojamientoController {
  private final ConsultarAlojamientoUseCase consultarAlojamientoUseCase;
  private final RegistrarTemporadaUseCase registrarTemporadaUseCase;
  private final ConsultarTemporadaUseCase consultarTemporadaUseCase;
  private final DefinirPoliticaCancelacionUseCase definirPoliticaCancelacionUseCase;
  private final ListarPoliticasCancelacionUseCase listarPoliticasCancelacionUseCase;
  private final ConsultarPoliticaCancelacionUseCase consultarPoliticaCancelacionUseCase;
  private final AlojamientoMapper mapper;

  public AlojamientoController(
      ConsultarAlojamientoUseCase consultarAlojamientoUseCase,
      RegistrarTemporadaUseCase registrarTemporadaUseCase,
      ConsultarTemporadaUseCase consultarTemporadaUseCase,
      DefinirPoliticaCancelacionUseCase definirPoliticaCancelacionUseCase,
      ListarPoliticasCancelacionUseCase listarPoliticasCancelacionUseCase,
      ConsultarPoliticaCancelacionUseCase consultarPoliticaCancelacionUseCase,
      AlojamientoMapper mapper) {
    this.consultarAlojamientoUseCase = consultarAlojamientoUseCase;
    this.registrarTemporadaUseCase = registrarTemporadaUseCase;
    this.consultarTemporadaUseCase = consultarTemporadaUseCase;
    this.definirPoliticaCancelacionUseCase = definirPoliticaCancelacionUseCase;
    this.listarPoliticasCancelacionUseCase = listarPoliticasCancelacionUseCase;
    this.consultarPoliticaCancelacionUseCase = consultarPoliticaCancelacionUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  public AlojamientoResponse consultar() {
    return mapper.toResponse(consultarAlojamientoUseCase.ejecutar());
  }

  @PostMapping("/temporadas")
  public ResponseEntity<TemporadaResponse> registrarTemporada(
      @Valid @RequestBody RegistrarTemporadaRequest request) {
    Temporada creada = registrarTemporadaUseCase.ejecutar(mapper.toDomain(request));
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creada.id().valor())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toTemporadaResponse(creada));
  }

  @GetMapping("/temporadas/{id}")
  public TemporadaResponse consultarTemporada(@PathVariable UUID id) {
    return mapper.toTemporadaResponse(consultarTemporadaUseCase.ejecutar(id));
  }

  @PostMapping("/politica-cancelacion")
  public ResponseEntity<PoliticaCancelacionResponse> definirPolitica(
      @Valid @RequestBody DefinirPoliticaCancelacionRequest request) {
    PoliticaCancelacion creada =
        definirPoliticaCancelacionUseCase.ejecutar(
            mapper.toTramos(request.tramos()), request.vigenteDesde());
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{version}")
            .buildAndExpand(creada.numeroVersion().numero())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toPoliticaResponse(creada));
  }

  @GetMapping("/politica-cancelacion")
  public Page<PoliticaCancelacionResponse> listarPoliticas(
      @PageableDefault(size = 10) Pageable pageable) {
    return listarPoliticasCancelacionUseCase.ejecutar(pageable).map(mapper::toPoliticaResponse);
  }

  @GetMapping("/politica-cancelacion/{version}")
  public PoliticaCancelacionResponse consultarPolitica(@PathVariable int version) {
    return mapper.toPoliticaResponse(consultarPoliticaCancelacionUseCase.ejecutar(version));
  }
}
