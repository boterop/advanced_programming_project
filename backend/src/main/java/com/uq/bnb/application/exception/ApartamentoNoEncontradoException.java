package com.uq.bnb.application.exception;

public class ApartamentoNoEncontradoException extends RuntimeException {
  public ApartamentoNoEncontradoException(String id) {
    super("No se encontró el apartamento con ID: " + id);
  }
}
