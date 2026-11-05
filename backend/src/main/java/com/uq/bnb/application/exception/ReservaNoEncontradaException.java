package com.uq.bnb.application.exception;

import com.uq.bnb.domain.valueobject.CodigoReserva;

/** No existe una reserva con ese código (404). */
public class ReservaNoEncontradaException extends RuntimeException {

    public ReservaNoEncontradaException(CodigoReserva codigo) {
        super("No se encontró la reserva con código: " + codigo.valor());
    }

    /** Para un código que ni siquiera tiene el formato RES-AAAA-NNNNN: tampoco existe ninguna reserva con él. */
    public ReservaNoEncontradaException(String codigoSinFormato) {
        super("No se encontró la reserva con código: " + codigoSinFormato);
    }
}
