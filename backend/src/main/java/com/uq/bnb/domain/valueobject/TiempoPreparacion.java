package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;

// RN-20: tiempo mínimo entre la salida de un grupo y la entrada del siguiente
// L-13: el valor lo define cada equipo, nunca se quema en el código
/** Objeto de valor (Guía 05) */
public record TiempoPreparacion(int horas) {

    public TiempoPreparacion {
        if (horas < 0) {
            throw new ReglaDominioException("El tiempo de preparación no puede ser negativo.");
        }
    }
}
