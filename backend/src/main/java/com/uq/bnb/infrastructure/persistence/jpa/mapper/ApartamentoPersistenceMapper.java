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
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Traduce persistencia <-> dominio. Para volver al dominio usa las fábricas reconstruir(...), de modo que
 * el dominio no necesita setters, constructor vacío ni anotaciones de JPA. Es manual (sin MapStruct) porque
 * reconstruir objetos con identidad y fábricas estáticas es más claro y seguro escrito a mano.
 */
@Component
public class ApartamentoPersistenceMapper {

    public ApartamentoEntity toEntity(Apartamento apartamento) {
        TipoApartamento tipo = apartamento.tipo();
        ApartamentoEntity entity = new ApartamentoEntity(
                apartamento.identificacion(),
                apartamento.nombre(),
                apartamento.capacidad(),
                tipo == null ? null : tipo.nombreTipo(),
                tipo == null ? List.of() : tipo.caracteristicas(),
                apartamento.permiteMascotas(),
                apartamento.estadoOperativo(),
                apartamento.activo());

        apartamento.dormitorios().forEach(d ->
                entity.getDormitorios().add(new DormitorioEmbeddable(d.descripcion(), d.tipoCama())));
        apartamento.tarifas().forEach(t -> entity.agregarTarifa(
                new TarifaEntity(t.id(), t.temporadaId(), t.valorPorNoche().monto(), t.vigenteDesde())));
        apartamento.bloqueos().forEach(b -> entity.agregarBloqueo(
                new BloqueoEntity(b.id(), new EstanciaEmbeddable(b.rango().entrada(), b.rango().salida()), b.motivo())));
        apartamento.novedades().forEach(n -> entity.agregarNovedad(
                new NovedadEntity(n.id(), n.fecha(), n.autor(), n.descripcion(), n.gravedad())));
        return entity;
    }

    public Apartamento toDomain(ApartamentoEntity entity) {
        TipoApartamento tipo = entity.getTipoNombre() == null
                ? null
                : new TipoApartamento(entity.getTipoNombre(), entity.getTipoCaracteristicas());

        List<Dormitorio> dormitorios = entity.getDormitorios().stream()
                .map(d -> new Dormitorio(d.descripcion(), d.tipoCama()))
                .toList();
        List<Tarifa> tarifas = entity.getTarifas().stream()
                .map(t -> Tarifa.reconstruir(t.getId(), t.getTemporadaId(), new Dinero(t.getValorPorNoche()), t.getVigenteDesde()))
                .toList();
        List<Bloqueo> bloqueos = entity.getBloqueos().stream()
                .map(b -> Bloqueo.reconstruir(b.getId(), new Estancia(b.getRango().entrada(), b.getRango().salida()), b.getMotivo()))
                .toList();
        List<Novedad> novedades = entity.getNovedades().stream()
                .map(n -> Novedad.reconstruir(n.getId(), n.getFecha(), n.getAutor(), n.getDescripcion(), n.getGravedad()))
                .toList();

        return Apartamento.reconstruir(entity.getIdentificacion(), entity.getNombre(), entity.getCapacidad(),
                dormitorios, tipo, entity.isPermiteMascotas(), entity.getEstadoOperativo(), tarifas, bloqueos, novedades,
                entity.isActivo());
    }
}
