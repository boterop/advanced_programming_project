package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.application.dto.request.BloquearApartamentoRequest;
import com.uq.bnb.application.dto.request.BuscarDisponibilidadRequest;
import com.uq.bnb.application.dto.request.CambiarCapacidadRequest;
import com.uq.bnb.application.dto.request.CrearApartamentoRequest;
import com.uq.bnb.application.dto.request.DefinirTarifaRequest;
import com.uq.bnb.application.dto.response.ApartamentoResponse;
import com.uq.bnb.application.dto.response.BloqueoResponse;
import com.uq.bnb.application.dto.response.TarifaResponse;
import com.uq.bnb.application.usecase.BloquearApartamentoUseCase;
import com.uq.bnb.application.usecase.BuscarApartamentosDisponiblesUseCase;
import com.uq.bnb.application.usecase.CambiarCapacidadApartamentoUseCase;
import com.uq.bnb.application.usecase.ConsultarApartamentoUseCase;
import com.uq.bnb.application.usecase.ConsultarBloqueoUseCase;
import com.uq.bnb.application.usecase.ConsultarTarifaUseCase;
import com.uq.bnb.application.usecase.CrearApartamentoUseCase;
import com.uq.bnb.application.usecase.DefinirTarifaUseCase;
import com.uq.bnb.application.usecase.DesbloquearApartamentoUseCase;
import com.uq.bnb.application.usecase.IniciarPreparacionApartamentoUseCase;
import com.uq.bnb.application.usecase.ListarApartamentosUseCase;
import com.uq.bnb.application.usecase.ListarBloqueosApartamentoUseCase;
import com.uq.bnb.application.usecase.ListarTarifasApartamentoUseCase;
import com.uq.bnb.application.usecase.MarcarApartamentoPreparadoUseCase;
import com.uq.bnb.application.usecase.RetirarApartamentoUseCase;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.entity.Tarifa;
import com.uq.bnb.infrastructure.rest.mapper.ApartamentoMapper;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/apartamentos")
public class ApartamentoController {
  private final CrearApartamentoUseCase crearApartamentoUseCase;
  private final ConsultarApartamentoUseCase consultarApartamentoUseCase;
  private final ListarApartamentosUseCase listarApartamentosUseCase;
  private final BuscarApartamentosDisponiblesUseCase buscarApartamentosDisponiblesUseCase;
  private final CambiarCapacidadApartamentoUseCase cambiarCapacidadApartamentoUseCase;
  private final RetirarApartamentoUseCase retirarApartamentoUseCase;
  private final BloquearApartamentoUseCase bloquearApartamentoUseCase;
  private final ListarBloqueosApartamentoUseCase listarBloqueosApartamentoUseCase;
  private final ConsultarBloqueoUseCase consultarBloqueoUseCase;
  private final DesbloquearApartamentoUseCase desbloquearApartamentoUseCase;
  private final DefinirTarifaUseCase definirTarifaUseCase;
  private final ListarTarifasApartamentoUseCase listarTarifasApartamentoUseCase;
  private final ConsultarTarifaUseCase consultarTarifaUseCase;
  private final IniciarPreparacionApartamentoUseCase iniciarPreparacionApartamentoUseCase;
  private final MarcarApartamentoPreparadoUseCase marcarApartamentoPreparadoUseCase;
  private final ApartamentoMapper mapper;

  public ApartamentoController(
      CrearApartamentoUseCase crearApartamentoUseCase,
      ConsultarApartamentoUseCase consultarApartamentoUseCase,
      ListarApartamentosUseCase listarApartamentosUseCase,
      BuscarApartamentosDisponiblesUseCase buscarApartamentosDisponiblesUseCase,
      CambiarCapacidadApartamentoUseCase cambiarCapacidadApartamentoUseCase,
      RetirarApartamentoUseCase retirarApartamentoUseCase,
      BloquearApartamentoUseCase bloquearApartamentoUseCase,
      ListarBloqueosApartamentoUseCase listarBloqueosApartamentoUseCase,
      ConsultarBloqueoUseCase consultarBloqueoUseCase,
      DesbloquearApartamentoUseCase desbloquearApartamentoUseCase,
      DefinirTarifaUseCase definirTarifaUseCase,
      ListarTarifasApartamentoUseCase listarTarifasApartamentoUseCase,
      ConsultarTarifaUseCase consultarTarifaUseCase,
      IniciarPreparacionApartamentoUseCase iniciarPreparacionApartamentoUseCase,
      MarcarApartamentoPreparadoUseCase marcarApartamentoPreparadoUseCase,
      ApartamentoMapper mapper) {
    this.crearApartamentoUseCase = crearApartamentoUseCase;
    this.consultarApartamentoUseCase = consultarApartamentoUseCase;
    this.listarApartamentosUseCase = listarApartamentosUseCase;
    this.buscarApartamentosDisponiblesUseCase = buscarApartamentosDisponiblesUseCase;
    this.cambiarCapacidadApartamentoUseCase = cambiarCapacidadApartamentoUseCase;
    this.retirarApartamentoUseCase = retirarApartamentoUseCase;
    this.bloquearApartamentoUseCase = bloquearApartamentoUseCase;
    this.listarBloqueosApartamentoUseCase = listarBloqueosApartamentoUseCase;
    this.consultarBloqueoUseCase = consultarBloqueoUseCase;
    this.desbloquearApartamentoUseCase = desbloquearApartamentoUseCase;
    this.definirTarifaUseCase = definirTarifaUseCase;
    this.listarTarifasApartamentoUseCase = listarTarifasApartamentoUseCase;
    this.consultarTarifaUseCase = consultarTarifaUseCase;
    this.iniciarPreparacionApartamentoUseCase = iniciarPreparacionApartamentoUseCase;
    this.marcarApartamentoPreparadoUseCase = marcarApartamentoPreparadoUseCase;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<ApartamentoResponse> crear(
      @Valid @RequestBody CrearApartamentoRequest request) {
    Apartamento creado = crearApartamentoUseCase.ejecutar(mapper.toDomain(request));
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creado.id().valor())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toResponse(creado));
  }

  @GetMapping
  public Page<ApartamentoResponse> listar(@PageableDefault(size = 10) Pageable pageable) {
    return listarApartamentosUseCase.ejecutar(pageable).map(mapper::toResponse);
  }

  @GetMapping("/disponibles")
  public Page<ApartamentoResponse> buscarDisponibles(
      @Valid @ModelAttribute BuscarDisponibilidadRequest request,
      @PageableDefault(size = 10) Pageable pageable) {
    return buscarApartamentosDisponiblesUseCase
        .ejecutar(
            mapper.toEstancia(request),
            mapper.toTotalOcupantes(request),
            mapper.toConMascota(request),
            pageable)
        .map(mapper::toResponse);
  }

  @GetMapping("/{id}")
  public ApartamentoResponse consultar(@PathVariable UUID id) {
    return mapper.toResponse(consultarApartamentoUseCase.ejecutar(id.toString()));
  }

  @PutMapping("/{id}/capacidad")
  public ApartamentoResponse cambiarCapacidad(
      @PathVariable UUID id, @Valid @RequestBody CambiarCapacidadRequest request) {
    return mapper.toResponse(
        cambiarCapacidadApartamentoUseCase.ejecutar(id.toString(), request.capacidad()));
  }

  @PutMapping("/{id}/retirar")
  public ApartamentoResponse retirar(@PathVariable UUID id) {
    return mapper.toResponse(retirarApartamentoUseCase.ejecutar(id.toString()));
  }

  @PostMapping("/{id}/bloquear")
  public ResponseEntity<BloqueoResponse> bloquear(
      @PathVariable UUID id,
      @Valid @RequestBody BloquearApartamentoRequest request) {
    Bloqueo creado = bloquearApartamentoUseCase.ejecutar(id.toString(), mapper.toBloqueo(request));
    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/apartamentos/{apartamentoId}/bloqueos/{bloqueoId}")
            .buildAndExpand(id, creado.id().valor())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toBloqueoResponse(id.toString(), creado));
  }

  @GetMapping("/{id}/bloqueos")
  public Page<BloqueoResponse> listarBloqueos(
      @PathVariable UUID id, @PageableDefault(size = 10) Pageable pageable) {
    return listarBloqueosApartamentoUseCase
        .ejecutar(id.toString(), pageable)
        .map(bloqueo -> mapper.toBloqueoResponse(id.toString(), bloqueo));
  }

  @GetMapping("/{apartamentoId}/bloqueos/{bloqueoId}")
  public BloqueoResponse consultarBloqueo(
      @PathVariable UUID apartamentoId, @PathVariable UUID bloqueoId) {
    return mapper.toBloqueoResponse(
        apartamentoId.toString(),
        consultarBloqueoUseCase.ejecutar(apartamentoId.toString(), bloqueoId));
  }

  @DeleteMapping("/{apartamentoId}/bloqueos/{bloqueoId}")
  public ResponseEntity<Void> desbloquear(
      @PathVariable UUID apartamentoId, @PathVariable UUID bloqueoId) {
    desbloquearApartamentoUseCase.ejecutar(apartamentoId.toString(), bloqueoId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{id}/tarifas")
  public ResponseEntity<TarifaResponse> definirTarifa(
      @PathVariable UUID id, @Valid @RequestBody DefinirTarifaRequest request) {
    Tarifa creada =
        definirTarifaUseCase.ejecutar(
            id.toString(),
            request.temporadaId(),
            mapper.toDinero(request.valorPorNoche()),
            request.vigenteDesde());
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(creada.id().valor())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toTarifaResponse(creada));
  }

  @GetMapping("/{id}/tarifas")
  public Page<TarifaResponse> listarTarifas(
      @PathVariable UUID id,
      @RequestParam(required = false) UUID temporadaId,
      @PageableDefault(size = 10) Pageable pageable) {
    return listarTarifasApartamentoUseCase
        .ejecutar(id.toString(), temporadaId, pageable)
        .map(mapper::toTarifaResponse);
  }

  @GetMapping("/{apartamentoId}/tarifas/{tarifaId}")
  public TarifaResponse consultarTarifa(
      @PathVariable UUID apartamentoId, @PathVariable UUID tarifaId) {
    return mapper.toTarifaResponse(
        consultarTarifaUseCase.ejecutar(apartamentoId.toString(), tarifaId));
  }

  @PutMapping("/{id}/iniciar-preparacion")
  public ApartamentoResponse iniciarPreparacion(@PathVariable UUID id) {
    return mapper.toResponse(iniciarPreparacionApartamentoUseCase.ejecutar(id.toString()));
  }

  @PutMapping("/{id}/marcar-preparado")
  public ApartamentoResponse marcarPreparado(@PathVariable UUID id) {
    return mapper.toResponse(marcarApartamentoPreparadoUseCase.ejecutar(id.toString()));
  }
}
