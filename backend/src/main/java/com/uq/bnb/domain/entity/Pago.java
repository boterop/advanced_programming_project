package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.MedioPago;

import java.time.LocalDate;
import java.util.Objects;


@Getter
public final class Pago {

    private final ID id;
    private final MedioPago medio;
    private final LocalDate fecha;
    private final Dinero valor;

    private Pago(ID id, MedioPago medio, LocalDate fecha, Dinero valor) {
        this.id = id;
        this.medio = medio;
        this.fecha = fecha;
        this.valor = valor;
    }

    public static Pago crear(ID id, MedioPago medio, LocalDate fecha, Dinero valor) {
        Objects.requireNonNull(medio, "El medio de pago no puede ser nulo");
        Objects.requireNonNull(fecha, "La fecha del pago no puede ser nula");
        Objects.requireNonNull(valor, "El valor del pago no puede ser nulo");
        if (valor.esNegativo() || valor.esCero()) {
            throw new ReglaDominioException("Un pago debe tener un valor positivo");
        }
        return new Pago(id, medio, fecha, valor);
    }


    public static Pago reconstruir(ID id, MedioPago medio, LocalDate fecha, Dinero valor) {
        Objects.requireNonNull(id, "El identificador del pago no puede ser nulo");
        Objects.requireNonNull(medio, "El medio de pago no puede ser nulo");
        Objects.requireNonNull(fecha, "La fecha del pago no puede ser nula");
        Objects.requireNonNull(valor, "El valor del pago no puede ser nulo");
        return new Pago(id, medio, fecha, valor);
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Pago entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
