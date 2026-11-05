package com.uq.bnb.application.exception;

import com.uq.bnb.domain.valueobject.CodigoReserva;

public class FolioNoEncontradoException extends RuntimeException {
    public FolioNoEncontradoException(CodigoReserva reserva) {
        super("No se encontró el folio de la reserva: " + reserva.valor());
    }
}
