package com.uq.bnb.application.exception;

public class AlojamientoNoEncontradoException extends RuntimeException {
  public AlojamientoNoEncontradoException() {
    super("El alojamiento todavía no ha sido configurado");
  }
}
