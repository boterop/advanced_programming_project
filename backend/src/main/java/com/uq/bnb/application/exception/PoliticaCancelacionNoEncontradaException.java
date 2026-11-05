package com.uq.bnb.application.exception;
public class PoliticaCancelacionNoEncontradaException extends RuntimeException {
    public PoliticaCancelacionNoEncontradaException(String detalle) {
        super("No se encontró la política de cancelación: " + detalle);
    }
}
