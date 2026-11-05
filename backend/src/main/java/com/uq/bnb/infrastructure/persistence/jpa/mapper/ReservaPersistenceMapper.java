package com.uq.bnb.infrastructure.persistence.jpa.mapper;

import com.uq.bnb.domain.entity.Cargo;
import com.uq.bnb.domain.entity.Folio;
import com.uq.bnb.domain.entity.Novedad;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.entity.Pago;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.entity.Titular;
import com.uq.bnb.domain.valueobject.Cancelacion;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.DetalleNoche;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.VersionPolitica;
import com.uq.bnb.infrastructure.persistence.jpa.entity.CargoEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.DetalleNocheEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.EstanciaEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.NovedadReservaEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.OcupanteEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.PagoEmbeddable;
import com.uq.bnb.infrastructure.persistence.jpa.entity.ReservaEntity;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ReservaPersistenceMapper {
  public ReservaEntity toEntity(Reserva reserva) {
    ReservaEntity entity =
        new ReservaEntity(
            java.util.UUID.fromString(reserva.id().valor()),
            reserva.codigo().valor(),
            reserva.apartamentoId(),
            toEmbeddable(reserva.titular()),
            new EstanciaEmbeddable(reserva.estancia().entrada(), reserva.estancia().salida()),
            reserva.canalOrigen(),
            reserva.estado(),
            reserva.versionPolitica().numero(),
            reserva.versionPolitica().vigenteDesde(),
            reserva.fechaCreacion(),
            reserva.conMascota(),
            reserva.horaEstimadaLlegada(),
            java.util.UUID.fromString(reserva.folio().id().valor()));
    java.util.Optional.ofNullable(reserva.cancelacion())
        .ifPresent(
            c ->
                entity.asignarCancelacion(
                    c.motivo(), c.fecha(), c.retenido().monto(), c.devuelto().monto()));
    reserva
        .cotizacion()
        .desglosePorNoche()
        .forEach(
            d ->
                entity
                    .getDetalleNoches()
                    .add(
                        new DetalleNocheEmbeddable(
                            d.fecha(),
                            d.nombreTemporada(),
                            d.tarifaAplicada().monto(),
                            d.ocupantesFacturables())));
    reserva.ocupantes().forEach(o -> entity.getOcupantes().add(toEmbeddable(o)));
    reserva
        .folio()
        .cargos()
        .forEach(
            c ->
                entity
                    .getCargos()
                    .add(
                        new CargoEmbeddable(
                            java.util.UUID.fromString(c.id().valor()),
                            c.tipo(),
                            c.valor().monto())));
    reserva
        .folio()
        .pagos()
        .forEach(
            p ->
                entity
                    .getPagos()
                    .add(
                        new PagoEmbeddable(
                            java.util.UUID.fromString(p.id().valor()),
                            p.medio(),
                            p.fecha(),
                            p.valor().monto())));
    reserva
        .novedades()
        .forEach(
            n ->
                entity
                    .getNovedades()
                    .add(
                        new NovedadReservaEmbeddable(
                            java.util.UUID.fromString(n.id().valor()),
                            n.fecha(),
                            n.autor(),
                            n.descripcion(),
                            n.gravedad())));
    return entity;
  }

  public Reserva toDomain(ReservaEntity entity) {
    Titular titular =
        Titular.crear(
            new com.uq.bnb.domain.valueobject.ID(entity.getTitular().id().toString()),
            entity.getTitular().documento(),
            entity.getTitular().nombre(),
            entity.getTitular().fechaNacimiento());
    Estancia estancia = new Estancia(entity.getEstancia().entrada(), entity.getEstancia().salida());
    List<DetalleNoche> desglose =
        entity.getDetalleNoches().stream()
            .map(
                d ->
                    new DetalleNoche(
                        d.fecha(),
                        d.nombreTemporada(),
                        new Dinero(d.tarifaAplicada()),
                        d.ocupantesFacturables()))
            .toList();
    List<Ocupante> ocupantes =
        entity.getOcupantes().stream()
            .map(
                o ->
                    Ocupante.crear(
                        new com.uq.bnb.domain.valueobject.ID(o.id().toString()),
                        o.documento(),
                        o.nombre(),
                        o.fechaNacimiento()))
            .toList();
    List<Cargo> cargos =
        entity.getCargos().stream()
            .map(
                c ->
                    Cargo.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(c.id().toString()),
                        c.tipo(),
                        new Dinero(c.valor())))
            .toList();
    List<Pago> pagos =
        entity.getPagos().stream()
            .map(
                p ->
                    Pago.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(p.id().toString()),
                        p.medio(),
                        p.fecha(),
                        new Dinero(p.valor())))
            .toList();
    List<Novedad> novedades =
        entity.getNovedades().stream()
            .map(
                n ->
                    Novedad.reconstruir(
                        new com.uq.bnb.domain.valueobject.ID(n.id().toString()),
                        n.fecha(),
                        n.autor(),
                        n.descripcion(),
                        n.gravedad()))
            .toList();
    Cancelacion cancelacion =
        entity.getMotivoCancelacion() == null
            ? null
            : new Cancelacion(
                entity.getMotivoCancelacion(),
                entity.getFechaCancelacion(),
                new Dinero(entity.getMontoRetenido()),
                new Dinero(entity.getMontoDevuelto()));
    return Reserva.reconstruir(
        new com.uq.bnb.domain.valueobject.ID(entity.getId().toString()),
        new CodigoReserva(entity.getCodigo()),
        entity.getApartamentoId(),
        titular,
        estancia,
        entity.getCanalOrigen(),
        entity.getEstado(),
        new Cotizacion(desglose),
        new VersionPolitica(
            entity.getVersionPoliticaNumero(), entity.getVersionPoliticaVigenteDesde()),
        entity.getFechaCreacion(),
        entity.isConMascota(),
        entity.getHoraEstimadaLlegada(),
        cancelacion,
        Folio.reconstruir(
            new com.uq.bnb.domain.valueobject.ID(entity.getFolioId().toString()),
            new com.uq.bnb.domain.valueobject.ID(entity.getId().toString()),
            cargos,
            pagos),
        ocupantes,
        novedades);
  }

  private static OcupanteEmbeddable toEmbeddable(Ocupante ocupante) {
    return new OcupanteEmbeddable(
        java.util.UUID.fromString(ocupante.id().valor()),
        ocupante.documento(),
        ocupante.nombre(),
        ocupante.fechaNacimiento());
  }
}
