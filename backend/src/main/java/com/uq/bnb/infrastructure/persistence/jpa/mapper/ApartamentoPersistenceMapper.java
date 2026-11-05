package com.uq.bnb.infrastructure.persistence.jpa.mapper;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.entity.Novedad;
import com.uq.bnb.domain.entity.Tarifa;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.TipoApartamento;
import com.uq.bnb.infrastructure.persistence.jpa.entity.ApartamentoEntity;
import com.uq.bnb.infrastructure.persistence.jpa.entity.BloqueoEntity;
import com.uq.bnb.infrastructure.persistence.jpa.entity.DormitorioEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.EstanciaEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.NovedadEntity;
import com.uq.bnb.infrastructure.persistence.jpa.entity.TarifaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ApartamentoPersistenceMapper {
  public ApartamentoEntity toEntity(Apartamento apartamento) {
    TipoApartamento tipo = apartamento.tipo();
    ApartamentoEntity entity =
        new ApartamentoEntity(
            java.util.UUID.fromString(apartamento.id().valor()),
            apartamento.identificacion(),
            apartamento.nombre(),
            apartamento.capacidad(),
            tipo == null ? null : tipo.nombreTipo(),
            tipo == null ? List.of() : tipo.caracteristicas(),
            apartamento.permiteMascotas(),
            apartamento.estadoOperativo(),
            apartamento.activo());
    apartamento
        .dormitorios()
        .forEach(
            d ->
                entity
                    .getDormitorios()
                    .add(new DormitorioEmbeddable(d.descripcion(), d.tipoCama())));
    apartamento
        .tarifas()
        .forEach(
            t ->
                entity.agregarTarifa(
                    new TarifaEntity(
                        java.util.UUID.fromString(t.id().valor()),
                        java.util.UUID.fromString(t.temporadaId().valor()),
                        t.valorPorNoche().monto(),
                        t.vigenteDesde())));
    apartamento
        .bloqueos()
        .forEach(
            b ->
                entity.agregarBloqueo(
                    new BloqueoEntity(
                        java.util.UUID.fromString(b.id().valor()),
                        new EstanciaEmbeddable(b.rango().entrada(), b.rango().salida()),
                        b.motivo())));
    apartamento
        .novedades()
        .forEach(
            n ->
                entity.agregarNovedad(
                    new NovedadEntity(
                        java.util.UUID.fromString(n.id().valor()),
                        n.fecha(),
                        n.autor(),
                        n.descripcion(),
                        n.gravedad())));
    return entity;
  }

  public Apartamento toDomain(ApartamentoEntity entity) {
    TipoApartamento tipo =
        entity.getTipoNombre() == null
            ? null
            : new TipoApartamento(entity.getTipoNombre(), entity.getTipoCaracteristicas());
    List<Dormitorio> dormitorios =
        entity.getDormitorios().stream()
            .map(d -> new Dormitorio(d.descripcion(), d.tipoCama()))
            .toList();
    List<Tarifa> tarifas =
        entity.getTarifas().stream()
            .map(
                t ->
                    Tarifa.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(t.getId().toString()),
                        new com.uq.bnb.domain.valueobject.ID(t.getTemporadaId().toString()),
                        new Dinero(t.getValorPorNoche()),
                        t.getVigenteDesde()))
            .toList();
    List<Bloqueo> bloqueos =
        entity.getBloqueos().stream()
            .map(
                b ->
                    Bloqueo.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(b.getId().toString()),
                        new Estancia(b.getRango().entrada(), b.getRango().salida()),
                        b.getMotivo()))
            .toList();
    List<Novedad> novedades =
        entity.getNovedades().stream()
            .map(
                n ->
                    Novedad.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(n.getId().toString()),
                        n.getFecha(),
                        n.getAutor(),
                        n.getDescripcion(),
                        n.getGravedad()))
            .toList();
    return Apartamento.reconstruir(
        new com.uq.bnb.domain.valueobject.ID(entity.getUuid().toString()),
        entity.getIdentificacion(),
        entity.getNombre(),
        entity.getCapacidad(),
        dormitorios,
        tipo,
        entity.isPermiteMascotas(),
        entity.getEstadoOperativo(),
        tarifas,
        bloqueos,
        novedades,
        entity.isActivo());
  }
}
