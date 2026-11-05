package com.uq.bnb.application.exception;

import java.util.UUID;

public class TemporadaNoEncontradaException extends RuntimeException {

    public TemporadaNoEncontradaException(UUID id) {
        super("No se encontró la temporada con identificador: " + id);
    }
}
