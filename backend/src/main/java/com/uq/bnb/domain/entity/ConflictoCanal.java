package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.CodigoReserva;

import java.time.LocalDateTime;
import java.util.Objects;


@Getter
public final class ConflictoCanal {

    private final ID id;
    private final String idReservaExterna;
    private final CodigoReserva reservaVigente;
    private final String apartamentoId;
    private final LocalDateTime fechaHoraIntento;
    private final String observacion;
    private boolean revisado;

    private ConflictoCanal(ID id, String idReservaExterna, CodigoReserva reservaVigente, String apartamentoId,
                           LocalDateTime fechaHoraIntento, String observacion, boolean revisado) {
        this.id = id;
        this.idReservaExterna = idReservaExterna;
        this.reservaVigente = reservaVigente;
        this.apartamentoId = apartamentoId;
        this.fechaHoraIntento = fechaHoraIntento;
        this.observacion = observacion;
        this.revisado = revisado;
    }

    public static ConflictoCanal registrar(ID id, String idReservaExterna, CodigoReserva reservaVigente, String apartamentoId,
                                           LocalDateTime fechaHoraIntento, String observacion) {
        if (idReservaExterna == null || idReservaExterna.isBlank()) {
            throw new ReglaDominioException("La reserva externa no puede ser vacia");
        }
        if (reservaVigente == null) {
            throw new ReglaDominioException("La reserva Vigente no puede ser vacia");
        }
        if (apartamentoId == null || apartamentoId.isBlank()) {
            throw new ReglaDominioException("El id del apartamento no puede ser vacio");
        }
        if (fechaHoraIntento == null) {
            throw new ReglaDominioException("La fecha y hora del intento no puede ser vacio");
        }
        if (observacion == null) {
            throw new ReglaDominioException("La observación no puede ser vacia");
        }
        return new ConflictoCanal(id, idReservaExterna, reservaVigente, apartamentoId, fechaHoraIntento,
                observacion, false);
    }


    public void marcarRevisado() {
        if (revisado) {
            throw new ReglaDominioException("El conflicto de canal ya fue revisado");
        }
        this.revisado = true;
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof ConflictoCanal entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
