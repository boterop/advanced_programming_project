package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Estancia;

import java.util.Objects;


@Getter
public final class Bloqueo {

    private final ID id;
    private final Estancia rango;
    private final String motivo;

    private Bloqueo(ID id, Estancia rango, String motivo) {
        this.id = id;
        this.rango = rango;
        this.motivo = motivo;
    }

    public static Bloqueo crear(ID id, Estancia rango, String motivo) {
        Objects.requireNonNull(rango, "El rango de fechas del bloqueo no puede ser nulo");
        if (motivo == null || motivo.isBlank()) {
            throw new ReglaDominioException("El motivo del bloqueo no puede estar vacío");
        }
        return new Bloqueo(id, rango, motivo.trim());
    }


    public static Bloqueo reconstruir(ID id, Estancia rango, String motivo) {
        return new Bloqueo(id, rango, motivo);
    }


    public boolean seSolapaCon(Estancia estanciaConsultada) {
        Objects.requireNonNull(estanciaConsultada, "La estancia a consultar no puede ser nula");
        return this.rango.seSolapaCon(estanciaConsultada);
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Bloqueo entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
