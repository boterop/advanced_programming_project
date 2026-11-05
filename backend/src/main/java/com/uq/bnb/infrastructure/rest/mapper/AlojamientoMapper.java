package com.uq.bnb.infrastructure.rest.mapper;

import com.uq.bnb.application.dto.request.RegistrarTemporadaRequest;
import com.uq.bnb.application.dto.request.TramoRequest;
import com.uq.bnb.application.dto.response.AlojamientoResponse;
import com.uq.bnb.application.dto.response.ParametrosResponse;
import com.uq.bnb.application.dto.response.PoliticaCancelacionResponse;
import com.uq.bnb.application.dto.response.TemporadaResponse;
import com.uq.bnb.application.dto.response.TramoResponse;
import com.uq.bnb.application.usecase.ConsultarAlojamientoUseCase;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.entity.Temporada;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/** DTO <-> dominio sin lógica de negocio. El dominio usa accesores fluidos, por eso los campos se indican con expression. */
@Mapper(componentModel = "spring")
public interface AlojamientoMapper {

    /** Pasa por Temporada.crear(...): el request no puede saltarse las validaciones del dominio. */
    default Temporada toDomain(RegistrarTemporadaRequest request) {
        return Temporada.crear(request.nombre(), request.fechaInicio(), request.fechaFin());
    }

    default AlojamientoResponse toResponse(ConsultarAlojamientoUseCase.Resultado resultado) {
        Alojamiento alojamiento = resultado.alojamiento();
        return new AlojamientoResponse(
                alojamiento.nombre(),
                alojamiento.ciudad(),
                toParametrosResponse(alojamiento.parametros()),
                toTemporadaResponses(alojamiento.temporadas()),
                toPoliticaResponse(resultado.politicaVigente()));
    }

    @Mapping(target = "id", expression = "java(temporada.id())")
    @Mapping(target = "nombre", expression = "java(temporada.nombre())")
    @Mapping(target = "fechaInicio", expression = "java(temporada.fechaInicio())")
    @Mapping(target = "fechaFin", expression = "java(temporada.fechaFin())")
    @Mapping(target = "esBase", expression = "java(temporada.esBase())")
    TemporadaResponse toTemporadaResponse(Temporada temporada);

    List<TemporadaResponse> toTemporadaResponses(List<Temporada> temporadas);

    @Mapping(target = "umbralEdadFacturable", expression = "java(parametros.umbralEdadFacturable().anios())")
    @Mapping(target = "horaEntrada", expression = "java(parametros.horario().horaEntrada())")
    @Mapping(target = "horaSalida", expression = "java(parametros.horario().horaSalida())")
    @Mapping(target = "tiempoPreparacionHoras", expression = "java(parametros.tiempoPreparacion().horas())")
    @Mapping(target = "plazoConfirmacionHoras", expression = "java(parametros.plazoConfirmacion().horas())")
    @Mapping(target = "horaLimiteNoShow", expression = "java(parametros.horaLimiteNoShow().hora())")
    @Mapping(target = "exigeAnticipo", expression = "java(parametros.politicaAnticipo().exige())")
    @Mapping(target = "porcentajeAnticipo", expression = "java(parametros.politicaAnticipo().porcentaje())")
    ParametrosResponse toParametrosResponse(ParametrosAlojamiento parametros);

    @Mapping(target = "version", expression = "java(politica.numeroVersion().numero())")
    @Mapping(target = "vigenteDesde", expression = "java(politica.vigenteDesde())")
    @Mapping(target = "tramos", expression = "java(toTramoResponses(politica.tramos()))")
    PoliticaCancelacionResponse toPoliticaResponse(PoliticaCancelacion politica);

    @Mapping(target = "porcentajeDevolucion", expression = "java(tramo.porcentajeDevolucion())")
    TramoResponse toTramoResponse(TramoAntelacion tramo);

    List<TramoResponse> toTramoResponses(List<TramoAntelacion> tramos);

    /** Pasa por el constructor de TramoAntelacion: el request no puede saltarse las validaciones del dominio. */
    default TramoAntelacion toDomain(TramoRequest request) {
        return new TramoAntelacion(request.antelacionMinimaHoras(), request.porcentajeRetencion());
    }

    List<TramoAntelacion> toTramos(List<TramoRequest> requests);
}
