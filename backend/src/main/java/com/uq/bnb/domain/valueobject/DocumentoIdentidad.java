package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;

public record DocumentoIdentidad(String numero) {

    public DocumentoIdentidad{
        if (numero == null || numero.isBlank() || numero.isEmpty()){
            throw new ReglaDominioException("El documento de identidad es algo obligatorio");
        }

        numero = numero.trim();

    }


}
