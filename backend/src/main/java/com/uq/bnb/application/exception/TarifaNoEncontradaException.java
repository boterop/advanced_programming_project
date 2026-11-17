package com.uq.bnb.application.exception;

import java.util.UUID;

public class TarifaNoEncontradaException extends RuntimeException {
  public TarifaNoEncontradaException(String apartamentoId, UUID id) {
    super("No se encontró la tarifa " + id + " del apartamento " + apartamentoId);
  }
}
