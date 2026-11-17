package com.uq.bnb.application.exception;

import java.util.UUID;

public class BloqueoNoEncontradoException extends RuntimeException {
  public BloqueoNoEncontradoException(String apartamentoId, UUID id) {
    super("No se encontró el bloqueo " + id + " del apartamento " + apartamentoId);
  }
}
