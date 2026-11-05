package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Gravedad;

import java.time.LocalDate;
import java.util.Objects;


@Getter
public final class Novedad {

    private final ID id;
    private final LocalDate fecha;
    private final String autor;
    private final String descripcion;
    private final Gravedad gravedad;

    private Novedad(ID id, LocalDate fecha, String autor, String descripcion, Gravedad gravedad) {
        this.id = id;
        this.fecha = fecha;
        this.autor = autor;
        this.descripcion = descripcion;
        this.gravedad = gravedad;
    }

    public static Novedad crear(ID id, LocalDate fecha, String autor, String descripcion, Gravedad gravedad) {
        Objects.requireNonNull(fecha, "La fecha de la novedad no puede ser nula");
        if (autor == null || autor.isBlank()) {
            throw new ReglaDominioException("El autor de la novedad no puede estar vacío");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new ReglaDominioException("La descripción de la novedad no puede estar vacía");
        }
        Objects.requireNonNull(gravedad, "La gravedad de la novedad no puede ser nula");
        return new Novedad(id, fecha, autor, descripcion, gravedad);
    }


    public static Novedad reconstruir(ID id, LocalDate fecha, String autor, String descripcion, Gravedad gravedad) {
        return new Novedad(id, fecha, autor, descripcion, gravedad);
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Novedad entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
