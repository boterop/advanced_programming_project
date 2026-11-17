package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public final class Titular extends Ocupante {

  private Titular(ID id, String documento, String nombre, LocalDate fechaNacimiento) {
    super(id, documento, nombre, fechaNacimiento);
  }

  public static Titular crear(ID id, String documento, String nombre, LocalDate fechaNacimiento) {
    return new Titular(id, documento, nombre, fechaNacimiento);
  }
}
