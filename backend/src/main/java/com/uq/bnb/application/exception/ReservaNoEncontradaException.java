package com.uq.bnb.application.exception;

import com.uq.bnb.domain.valueobject.CodigoReserva;

public class ReservaNoEncontradaException extends RuntimeException {
  public ReservaNoEncontradaException(CodigoReserva codigo) {
    super("No se encontró la reserva con código: " + codigo.valor());
  }

  public ReservaNoEncontradaException(String codigoSinFormato) {
    super("No se encontró la reserva con código: " + codigoSinFormato);
  }
}
