package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.CodigoReserva;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;


@Getter
public final class Registro {

    private final ID id;
    private final LocalDate fechaEntrada;
    private final LocalTime horaEntrada;
    private final CodigoReserva codigoReserva;
    private final String apartamentoId;
    private final String documentoTitular;

    private Registro(ID id, LocalDate fechaEntrada, LocalTime horaEntrada, CodigoReserva codigoReserva,
                     String apartamentoId, String documentoTitular) {
        this.id = id;
        this.fechaEntrada = fechaEntrada;
        this.horaEntrada = horaEntrada;
        this.codigoReserva = codigoReserva;
        this.apartamentoId = apartamentoId;
        this.documentoTitular = documentoTitular;
    }

    public static Registro crear(ID id, LocalDate fechaEntrada, LocalTime horaEntrada, CodigoReserva codigoReserva,
                                 String apartamentoId, String documentoTitular) {
        if (fechaEntrada == null) {
            throw new ReglaDominioException("La fecha de entrada no puede ser vacia");
        }
        if (horaEntrada == null) {
            throw new ReglaDominioException("La hora de entrada no puede ser vacia");
        }
        if (codigoReserva == null) {
            throw new ReglaDominioException("La reserva no puede ser vacia");
        }
        if (apartamentoId == null || apartamentoId.isBlank()) {
            throw new ReglaDominioException("El apartamento no puede ser vacio");
        }
        if (documentoTitular == null || documentoTitular.isBlank()) {
            throw new ReglaDominioException("El titular no puede ser vacio");
        }
        return new Registro(id, fechaEntrada, horaEntrada, codigoReserva, apartamentoId, documentoTitular);
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Registro entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
