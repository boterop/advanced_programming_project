package com.uq.bnb.application.exception;
public class ApartamentoNoEncontradoException extends RuntimeException {
    public ApartamentoNoEncontradoException(String identificacion) {
        super("No se encontró el apartamento con identificación: " + identificacion);
    }
}
