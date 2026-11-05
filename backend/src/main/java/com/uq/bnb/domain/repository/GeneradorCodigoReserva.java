package com.uq.bnb.domain.repository;

import com.uq.bnb.domain.valueobject.CodigoReserva;

// Puerto del mismo tipo que un repositorio, aunque no calce en el molde obtener/guardar
public interface GeneradorCodigoReserva {

    CodigoReserva generar();
}
