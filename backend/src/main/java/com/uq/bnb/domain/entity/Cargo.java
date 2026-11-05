package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.TipoCargo;

import java.util.Objects;


@Getter
public final class Cargo {

    private final ID id;
    private final TipoCargo tipo;
    private final Dinero valor;

    private Cargo(ID id, TipoCargo tipo, Dinero valor) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
    }

    public static Cargo crear(ID id, TipoCargo tipo, Dinero valor) {
        Objects.requireNonNull(tipo, "El tipo de cargo no puede ser nulo");
        Objects.requireNonNull(valor, "El valor del cargo no puede ser nulo");
        return new Cargo(id, tipo, valor);
    }


    public static Cargo reconstruir(ID id, TipoCargo tipo, Dinero valor) {
        Objects.requireNonNull(id, "El identificador del cargo no puede ser nulo");
        Objects.requireNonNull(tipo, "El tipo de cargo no puede ser nulo");
        Objects.requireNonNull(valor, "El valor del cargo no puede ser nulo");
        return new Cargo(id, tipo, valor);
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Cargo entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
