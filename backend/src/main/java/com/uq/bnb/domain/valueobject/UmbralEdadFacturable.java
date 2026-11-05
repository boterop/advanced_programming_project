package com.uq.bnb.domain.valueobject;
import com.uq.bnb.domain.exception.ReglaDominioException;
public record UmbralEdadFacturable(int anios) {
    public UmbralEdadFacturable {
        if (anios < 0) {
            throw new ReglaDominioException("El umbral de edad facturable no puede ser negativo");
        }
    }
    public boolean alcanza(int edad) {
        return edad >= anios;
    }
}
