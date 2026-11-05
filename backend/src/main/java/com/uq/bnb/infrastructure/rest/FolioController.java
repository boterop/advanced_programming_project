package com.uq.bnb.infrastructure.rest;

import com.uq.bnb.application.dto.request.RegistrarPagoRequest;
import com.uq.bnb.application.dto.response.FolioResponse;
import com.uq.bnb.application.usecase.ConsultarFolioUseCase;
import com.uq.bnb.application.usecase.RegistrarPagoUseCase;
import com.uq.bnb.domain.entity.Folio;
import com.uq.bnb.infrastructure.rest.mapper.FolioMapper;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/reservas/{codigo}/folio")
public class FolioController {
  private final ConsultarFolioUseCase consultarFolioUseCase;
  private final RegistrarPagoUseCase registrarPagoUseCase;
  private final FolioMapper mapper;

  public FolioController(
      ConsultarFolioUseCase consultarFolioUseCase,
      RegistrarPagoUseCase registrarPagoUseCase,
      FolioMapper mapper) {
    this.consultarFolioUseCase = consultarFolioUseCase;
    this.registrarPagoUseCase = registrarPagoUseCase;
    this.mapper = mapper;
  }

  @GetMapping
  public FolioResponse consultar(@PathVariable String codigo) {
    Folio folio = consultarFolioUseCase.ejecutar(mapper.toCodigo(codigo));
    return mapper.toResponse(codigo, folio);
  }

  @PostMapping("/pagos")
  public ResponseEntity<FolioResponse> registrarPago(
      @PathVariable String codigo, @Valid @RequestBody RegistrarPagoRequest request) {
    Folio folio =
        registrarPagoUseCase.ejecutar(
            mapper.toCodigo(codigo),
            mapper.toDinero(request.valor()),
            request.medioPago(),
            request.referencia());
    URI location =
        ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/reservas/{codigo}/folio")
            .buildAndExpand(codigo)
            .toUri();
    return ResponseEntity.created(location).body(mapper.toResponse(codigo, folio));
  }
}
