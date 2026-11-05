package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class Temporada {

  private final ID id;
  private final String nombre;
  private final LocalDate fechaInicio;
  private final LocalDate fechaFin;
  private final boolean esBase;

  private Temporada(
      ID id, String nombre, LocalDate fechaInicio, LocalDate fechaFin, boolean esBase) {
    this.id = id;
    this.nombre = nombre;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.esBase = esBase;
  }

  public static Temporada crear(ID id, String nombre, LocalDate fechaInicio, LocalDate fechaFin) {
    validarNombre(nombre);
    Objects.requireNonNull(fechaInicio, "La fecha de inicio no puede ser nula");
    Objects.requireNonNull(fechaFin, "La fecha de fin no puede ser nula");
    if (!fechaFin.isAfter(fechaInicio)) {
      throw new ReglaDominioException(
          "La fecha de fin de la temporada debe ser posterior a la fecha de inicio");
    }
    return new Temporada(id, nombre, fechaInicio, fechaFin, false);
  }

  public static Temporada crearBase(ID id, String nombre) {
    validarNombre(nombre);
    return new Temporada(id, nombre, null, null, true);
  }

  public static Temporada reconstruir(
      ID id, String nombre, LocalDate fechaInicio, LocalDate fechaFin, boolean esBase) {
    return new Temporada(id, nombre, fechaInicio, fechaFin, esBase);
  }

  private static void validarNombre(String nombre) {
    if (nombre == null || nombre.isBlank()) {
      throw new ReglaDominioException("El nombre de la temporada no puede estar vacío");
    }
  }

  public boolean cubre(LocalDate fecha) {
    Objects.requireNonNull(fecha, "La fecha no puede ser nula");
    if (esBase) {
      return true;
    }
    return !fecha.isBefore(fechaInicio) && fecha.isBefore(fechaFin);
  }

  public boolean seSolapaCon(Temporada otra) {
    Objects.requireNonNull(otra, "La temporada a comparar no puede ser nula");
    if (this.esBase || otra.esBase) {
      return false;
    }
    return this.fechaInicio.isBefore(otra.fechaFin) && otra.fechaInicio.isBefore(this.fechaFin);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Temporada entity && id.equals(entity.id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
