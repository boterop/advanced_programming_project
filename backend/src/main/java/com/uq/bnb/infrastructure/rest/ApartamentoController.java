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
            .path("/{identificacion}")
            .buildAndExpand(creado.identificacion())
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

  @GetMapping("/{identificacion}")
  public ApartamentoResponse consultar(@PathVariable String identificacion) {
    return mapper.toResponse(consultarApartamentoUseCase.ejecutar(identificacion));
  }

  @PutMapping("/{identificacion}/capacidad")
  public ApartamentoResponse cambiarCapacidad(
      @PathVariable String identificacion, @Valid @RequestBody CambiarCapacidadRequest request) {
    return mapper.toResponse(
        cambiarCapacidadApartamentoUseCase.ejecutar(identificacion, request.capacidad()));
  }

  @PutMapping("/{identificacion}/retirar")
  public ApartamentoResponse retirar(@PathVariable String identificacion) {
    return mapper.toResponse(retirarApartamentoUseCase.ejecutar(identificacion));
  }

  @PostMapping("/{id}/bloquear")
  public ResponseEntity<BloqueoResponse> bloquear(
      @PathVariable("id") String identificacion,
      @Valid @RequestBody BloquearApartamentoRequest request) {
    Bloqueo creado = bloquearApartamentoUseCase.ejecutar(identificacion, mapper.toBloqueo(request));
    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/apartamentos/{identificacion}/bloqueos/{id}")
            .buildAndExpand(identificacion, creado.id().valor())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toBloqueoResponse(identificacion, creado));
  }

  @GetMapping("/{identificacion}/bloqueos")
  public Page<BloqueoResponse> listarBloqueos(
      @PathVariable String identificacion, @PageableDefault(size = 10) Pageable pageable) {
    return listarBloqueosApartamentoUseCase
        .ejecutar(identificacion, pageable)
        .map(bloqueo -> mapper.toBloqueoResponse(identificacion, bloqueo));
  }

  @GetMapping("/{identificacion}/bloqueos/{id}")
  public BloqueoResponse consultarBloqueo(
      @PathVariable String identificacion, @PathVariable UUID id) {
    return mapper.toBloqueoResponse(
        identificacion, consultarBloqueoUseCase.ejecutar(identificacion, id));
  }

  @PostMapping("/{identificacion}/tarifas")
  public ResponseEntity<TarifaResponse> definirTarifa(
      @PathVariable String identificacion, @Valid @RequestBody DefinirTarifaRequest request) {
    Tarifa creada =
        definirTarifaUseCase.ejecutar(
            identificacion,
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

  @GetMapping("/{identificacion}/tarifas")
  public Page<TarifaResponse> listarTarifas(
      @PathVariable String identificacion,
      @RequestParam(required = false) UUID temporadaId,
      @PageableDefault(size = 10) Pageable pageable) {
    return listarTarifasApartamentoUseCase
        .ejecutar(identificacion, temporadaId, pageable)
        .map(mapper::toTarifaResponse);
  }

  @GetMapping("/{identificacion}/tarifas/{id}")
  public TarifaResponse consultarTarifa(
      @PathVariable String identificacion, @PathVariable UUID id) {
    return mapper.toTarifaResponse(consultarTarifaUseCase.ejecutar(identificacion, id));
  }

  @PutMapping("/{identificacion}/iniciar-preparacion")
  public ApartamentoResponse iniciarPreparacion(@PathVariable String identificacion) {
    return mapper.toResponse(iniciarPreparacionApartamentoUseCase.ejecutar(identificacion));
  }

  @PutMapping("/{identificacion}/marcar-preparado")
  public ApartamentoResponse marcarPreparado(@PathVariable String identificacion) {
    return mapper.toResponse(marcarApartamentoPreparadoUseCase.ejecutar(identificacion));
  }
}
