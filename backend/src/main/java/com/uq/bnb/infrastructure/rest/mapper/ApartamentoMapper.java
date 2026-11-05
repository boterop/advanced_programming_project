package com.uq.bnb.infrastructure.rest.mapper;

import com.uq.bnb.application.dto.request.BloquearApartamentoRequest;
import com.uq.bnb.application.dto.request.BuscarDisponibilidadRequest;
import com.uq.bnb.application.dto.request.CrearApartamentoRequest;
import com.uq.bnb.application.dto.request.DormitorioRequest;
import com.uq.bnb.application.dto.response.ApartamentoResponse;
import com.uq.bnb.application.dto.response.BloqueoResponse;
import com.uq.bnb.application.dto.response.DormitorioResponse;
import com.uq.bnb.application.dto.response.TarifaResponse;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.entity.Tarifa;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.Estancia;
import java.math.BigDecimal;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ApartamentoMapper {
  @Autowired protected IDProvider idProvider;

  public Apartamento toDomain(CrearApartamentoRequest request) {
    return Apartamento.crear(
        idProvider.obtener(),
        request.identificacion(),
        request.nombre(),
        request.capacidad(),
        toDormitorios(request.dormitorios()),
        null,
        Boolean.TRUE.equals(request.permiteMascotas()),
        request.estadoOperativoInicial());
  }

  public Bloqueo toBloqueo(BloquearApartamentoRequest request) {
    return Bloqueo.crear(
        idProvider.obtener(),
        new Estancia(request.fechaInicio(), request.fechaFin()),
        request.motivo());
  }

  public Estancia toEstancia(BuscarDisponibilidadRequest request) {
    return new Estancia(request.fechaEntrada(), request.fechaSalida());
  }

  public int toTotalOcupantes(BuscarDisponibilidadRequest request) {
    return request.adultos() + (request.ninos() == null ? 0 : request.ninos());
  }

  public boolean toConMascota(BuscarDisponibilidadRequest request) {
    return Boolean.TRUE.equals(request.conMascota());
  }

  public Dinero toDinero(BigDecimal valor) {
    return Dinero.de(valor);
  }

  public abstract List<Dormitorio> toDormitorios(List<DormitorioRequest> requests);

  public abstract Dormitorio toDormitorio(DormitorioRequest request);

  @Mapping(target = "identificacion", expression = "java(apartamento.identificacion())")
  @Mapping(target = "nombre", expression = "java(apartamento.nombre())")
  @Mapping(target = "capacidad", expression = "java(apartamento.capacidad())")
  @Mapping(
      target = "dormitorios",
      expression = "java(toDormitorioResponses(apartamento.dormitorios()))")
  @Mapping(target = "permiteMascotas", expression = "java(apartamento.permiteMascotas())")
  @Mapping(target = "estadoOperativo", expression = "java(apartamento.estadoOperativo())")
  @Mapping(target = "activo", expression = "java(apartamento.activo())")
  public abstract ApartamentoResponse toResponse(Apartamento apartamento);

  public abstract List<DormitorioResponse> toDormitorioResponses(List<Dormitorio> dormitorios);

  public abstract DormitorioResponse toDormitorioResponse(Dormitorio dormitorio);

  public TarifaResponse toTarifaResponse(Tarifa tarifa) {
    return new TarifaResponse(
        java.util.UUID.fromString(tarifa.id().valor()),
        java.util.UUID.fromString(tarifa.temporadaId().valor()),
        tarifa.valorPorNoche().monto(),
        tarifa.vigenteDesde());
  }

  public abstract List<TarifaResponse> toTarifaResponses(List<Tarifa> tarifas);

  public BloqueoResponse toBloqueoResponse(String identificacionApartamento, Bloqueo bloqueo) {
    return new BloqueoResponse(
        java.util.UUID.fromString(bloqueo.id().valor()),
        identificacionApartamento,
        bloqueo.rango().entrada(),
        bloqueo.rango().salida(),
        bloqueo.motivo());
  }
}
