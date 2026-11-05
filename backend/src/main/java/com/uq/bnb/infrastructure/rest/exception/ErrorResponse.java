package com.uq.bnb.infrastructure.rest.exception;

import java.util.List;

public record ErrorResponse(String codigoError, String mensaje, List<String> detalles) {
  public static ErrorResponse de(String codigoError, String mensaje) {
    return new ErrorResponse(codigoError, mensaje, List.of());
  }
}
