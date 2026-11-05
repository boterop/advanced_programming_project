package com.uq.bnb.infrastructure.rest.mapper;

import com.uq.bnb.application.dto.request.CotizarEstanciaRequest;
import com.uq.bnb.application.dto.request.OcupanteRequest;
import com.uq.bnb.application.dto.response.CotizacionResponse;
import com.uq.bnb.application.dto.response.DetalleNocheResponse;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.DetalleNoche;
import com.uq.bnb.domain.valueobject.Estancia;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CotizacionMapper {
  @Autowired protected IDProvider idProvider;

  public Estancia toEstancia(CotizarEstanciaRequest request) {
    return new Estancia(request.fechaEntrada(), request.fechaSalida());
  }

  public Ocupante toOcupante(OcupanteRequest request) {
    return Ocupante.crear(
        idProvider.obtener(), request.documento(), request.nombre(), request.fechaNacimiento());
  }

  public abstract List<Ocupante> toOcupantes(List<OcupanteRequest> requests);

  public DetalleNocheResponse toDetalleResponse(DetalleNoche detalle) {
    return new DetalleNocheResponse(
        detalle.fecha(),
        detalle.nombreTemporada(),
        detalle.tarifaAplicada().monto().longValueExact(),
        detalle.ocupantesFacturables(),
        detalle.subtotal().monto().longValueExact());
  }

  public abstract List<DetalleNocheResponse> toDetalleResponses(List<DetalleNoche> detalles);

  public CotizacionResponse toResponse(
      String identificacionApartamento, Estancia estancia, Cotizacion cotizacion) {
    return new CotizacionResponse(
        identificacionApartamento,
        estancia.entrada(),
        estancia.salida(),
        cotizacion.totalNoches(),
        cotizacion.desglosePorNoche().get(0).ocupantesFacturables(),
        toDetalleResponses(cotizacion.desglosePorNoche()),
        cotizacion.valorTotal().monto().longValueExact(),
        cotizacion.valorTotal().moneda().name());
  }
}
