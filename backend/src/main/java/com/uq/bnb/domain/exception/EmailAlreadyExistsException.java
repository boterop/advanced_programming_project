package com.uq.bnb.domain.exception;

public class EmailAlreadyExistsException extends RuntimeException {

  public EmailAlreadyExistsException() {
    super("El correo ya está registrado");
  }
}
