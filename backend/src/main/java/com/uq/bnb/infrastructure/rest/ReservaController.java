package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.application.dto.request.CancelarReservaRequest;
import com.uq.bnb.application.dto.request.ConfirmarReservaRequest;
import com.uq.bnb.application.dto.request.ConsultarReservasRequest;
import com.uq.bnb.application.dto.request.CrearReservaRequest;
import com.uq.bnb.application.dto.response.ReservaDetalleResponse;
import com.uq.bnb.application.dto.response.ReservaResumenResponse;
import com.uq.bnb.application.usecase.CancelarReservaUseCase;
import com.uq.bnb.application.usecase.ConfirmarReservaUseCase;
import com.uq.bnb.application.usecase.ConsultarReservaUseCase;
import com.uq.bnb.application.usecase.ConsultarReservasUseCase;
import com.uq.bnb.application.usecase.CrearReservaUseCase;
import com.uq.bnb.application.usecase.DeclararNoShowUseCase;
import com.uq.bnb.application.usecase.RegistrarLlegadaUseCase;
import com.uq.bnb.application.usecase.RegistrarSalidaUseCase;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.infrastructure.rest.mapper.ReservaMapper;
import jakarta.validation.Valid;
import java.net.URI;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/reservas")
public class ReservaController {
  private final CrearReservaUseCase crearReservaUseCase;
  private final ConsultarReservaUseCase consultarReservaUseCase;
  private final ConsultarReservasUseCase consultarReservasUseCase;
  private final ConfirmarReservaUseCase confirmarReservaUseCase;
  private final CancelarReservaUseCase cancelarReservaUseCase;
  private final RegistrarLlegadaUseCase registrarLlegadaUseCase;
  private final RegistrarSalidaUseCase registrarSalidaUseCase;
  private final DeclararNoShowUseCase declararNoShowUseCase;
  private final ReservaMapper mapper;

  public ReservaController(
      CrearReservaUseCase crearReservaUseCase,
      ConsultarReservaUseCase consultarReservaUseCase,
      ConsultarReservasUseCase consultarReservasUseCase,
      ConfirmarReservaUseCase confirmarReservaUseCase,
      CancelarReservaUseCase cancelarReservaUseCase,
      RegistrarLlegadaUseCase registrarLlegadaUseCase,
      RegistrarSalidaUseCase registrarSalidaUseCase,
      DeclararNoShowUseCase declararNoShowUseCase,
      ReservaMapper mapper) {
    this.crearReservaUseCase = crearReservaUseCase;
    this.consultarReservaUseCase = consultarReservaUseCase;
    this.consultarReservasUseCase = consultarReservasUseCase;
    this.confirmarReservaUseCase = confirmarReservaUseCase;
    this.cancelarReservaUseCase = cancelarReservaUseCase;
    this.registrarLlegadaUseCase = registrarLlegadaUseCase;
    this.registrarSalidaUseCase = registrarSalidaUseCase;
    this.declararNoShowUseCase = declararNoShowUseCase;
    this.mapper = mapper;
  }

  @PostMapping
  public ResponseEntity<ReservaDetalleResponse> crear(
      @Valid @RequestBody CrearReservaRequest request) {
    Reserva creada =
        crearReservaUseCase.ejecutar(
            request.identificacionApartamento(), mapper.toSolicitud(request));
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{codigo}")
            .buildAndExpand(creada.codigo().valor())
            .toUri();
    return ResponseEntity.created(location).body(mapper.toDetalleResponse(creada));
  }

  @GetMapping
  public Page<ReservaResumenResponse> consultar(
      @Valid @ModelAttribute ConsultarReservasRequest filtros,
      @PageableDefault(size = 10) Pageable pageable) {
    return consultarReservasUseCase
        .ejecutar(
            filtros.estado(),
            filtros.identificacionApartamento(),
            filtros.desde(),
            filtros.hasta(),
            pageable)
        .map(mapper::toResumenResponse);
  }

  @GetMapping("/{codigo}")
  public ReservaDetalleResponse consultarPorCodigo(@PathVariable String codigo) {
    return mapper.toDetalleResponse(consultarReservaUseCase.ejecutar(mapper.toCodigo(codigo)));
  }

  @PutMapping("/{codigo}/confirmar")
  public ReservaDetalleResponse confirmar(
      @PathVariable String codigo,
      @Valid @RequestBody(required = false) ConfirmarReservaRequest request) {
    Reserva confirmada =
        confirmarReservaUseCase.ejecutar(
            mapper.toCodigo(codigo),
            mapper.toHoraEstimadaLlegada(request),
            mapper.toMedioPago(request),
            mapper.toReferencia(request));
    return mapper.toDetalleResponse(confirmada);
  }

  @PutMapping("/{codigo}/cancelar")
  public ReservaDetalleResponse cancelar(
      @PathVariable String codigo, @Valid @RequestBody CancelarReservaRequest request) {
    return mapper.toDetalleResponse(
        cancelarReservaUseCase.ejecutar(mapper.toCodigo(codigo), request.motivo()));
  }

  @PutMapping("/{codigo}/registrar-llegada")
  public ReservaDetalleResponse registrarLlegada(@PathVariable String codigo) {
    return mapper.toDetalleResponse(registrarLlegadaUseCase.ejecutar(mapper.toCodigo(codigo)));
  }

  @PutMapping("/{codigo}/registrar-salida")
  public ReservaDetalleResponse registrarSalida(@PathVariable String codigo) {
    return mapper.toDetalleResponse(registrarSalidaUseCase.ejecutar(mapper.toCodigo(codigo)));
  }

  @PutMapping("/{codigo}/no-show")
  public ReservaDetalleResponse declararNoShow(@PathVariable String codigo) {
    return mapper.toDetalleResponse(declararNoShowUseCase.ejecutar(mapper.toCodigo(codigo)));
  }
}
