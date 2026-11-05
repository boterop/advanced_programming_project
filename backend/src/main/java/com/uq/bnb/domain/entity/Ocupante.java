package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import java.time.Period;
import lombok.Getter;

@Getter
public class Ocupante {
  private final ID id;
  private final String documento;
  private final String nombre;
  private final LocalDate fechaNacimiento;

  Ocupante(ID id, String documento, String nombre, LocalDate fechaNacimiento) {
    if (id == null) {
      throw new ReglaDominioException("El ID del ocupante no puede ser nulo");
    }
    if (documento == null || documento.isBlank()) {
      throw new ReglaDominioException("El documento del ocupante no puede estar vacío");
    }
    if (nombre == null || nombre.isBlank()) {
      throw new ReglaDominioException("El nombre del ocupante no puede estar vacío");
    }
    if (fechaNacimiento == null) {
      throw new ReglaDominioException("La fecha de nacimiento no puede ser nula");
    }
    this.id = id;
    this.documento = documento;
    this.nombre = nombre;
    this.fechaNacimiento = fechaNacimiento;
  }

  public static Ocupante crear(ID id, String documento, String nombre, LocalDate fechaNacimiento) {
    return new Ocupante(id, documento, nombre, fechaNacimiento);
  }

  public boolean esFacturable(LocalDate fecha, int edadFacturable) {
    return edad(fecha) >= edadFacturable;
  }

  public int edad() {
    return edad(LocalDate.now());
  }

  public int edad(LocalDate fecha) {
    return Period.between(fechaNacimiento, fecha).getYears();
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Ocupante o && id.equals(o.id());
  }
}
