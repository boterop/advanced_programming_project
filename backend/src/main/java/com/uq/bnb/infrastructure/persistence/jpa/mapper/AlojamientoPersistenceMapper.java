package com.uq.bnb.infrastructure.persistence.jpa.mapper;

import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.entity.Temporada;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import com.uq.bnb.domain.valueobject.VersionPolitica;
import com.uq.bnb.infrastructure.persistence.jpa.entity.AlojamientoEntity;
import com.uq.bnb.infrastructure.persistence.jpa.entity.PoliticaCancelacionEntity;
import com.uq.bnb.infrastructure.persistence.jpa.entity.TemporadaEntity;
import com.uq.bnb.infrastructure.persistence.jpa.entity.TramoEmbeddable;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class AlojamientoPersistenceMapper {
  public AlojamientoEntity toEntity(Alojamiento alojamiento) {
    AlojamientoEntity entity =
        new AlojamientoEntity(
            java.util.UUID.fromString(alojamiento.id().valor()),
            alojamiento.nombre(),
            alojamiento.ciudad());
    alojamiento
        .temporadas()
        .forEach(
            t ->
                entity.agregarTemporada(
                    new TemporadaEntity(
                        java.util.UUID.fromString(t.id().valor()),
                        t.nombre(),
                        t.fechaInicio(),
                        t.fechaFin(),
                        t.esBase())));
    alojamiento
        .historialPoliticas()
        .forEach(
            p ->
                entity.agregarPolitica(
                    new PoliticaCancelacionEntity(
                        java.util.UUID.fromString(p.id().valor()),
                        p.numeroVersion().numero(),
                        p.vigenteDesde(),
                        p.tramos().stream()
                            .map(
                                t ->
                                    new TramoEmbeddable(
                                        t.antelacionMinimaHoras(), t.porcentajeRetencion()))
                            .toList())));
    return entity;
  }

  public Alojamiento toDomain(AlojamientoEntity entity, ParametrosAlojamiento parametros) {
    List<Temporada> temporadas =
        entity.getTemporadas().stream()
            .map(
                t ->
                    Temporada.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(t.getId().toString()),
                        t.getNombre(),
                        t.getFechaInicio(),
                        t.getFechaFin(),
                        t.isEsBase()))
            .toList();
    List<PoliticaCancelacion> politicas =
        entity.getPoliticas().stream()
            .sorted(Comparator.comparingInt(PoliticaCancelacionEntity::getNumeroVersion))
            .map(
                p ->
                    PoliticaCancelacion.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(p.getId().toString()),
                        new VersionPolitica(p.getNumeroVersion(), p.getVigenteDesde()),
                        p.getTramos().stream()
                            .map(
                                t ->
                                    new TramoAntelacion(
                                        t.antelacionMinimaHoras(), t.porcentajeRetencion()))
                            .toList()))
            .toList();
    return Alojamiento.reconstruir(
        new com.uq.bnb.domain.valueobject.ID(entity.getId().toString()),
        entity.getNombre(),
        entity.getCiudad(),
        parametros,
        temporadas,
        politicas);
  }
}
