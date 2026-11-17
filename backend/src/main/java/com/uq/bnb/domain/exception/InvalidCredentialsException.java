package com.uq.bnb.domain.exception;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super("Credenciales inválidas");
  }
}
