package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class CodigoReserva(
    var codigo: String,
) {
    init {
        if (codigo.isBlank() || codigo.isEmpty()) throw ReglaDominioException("El codigo no puede ser nulo")
        codigo = codigo.trim()
    }
}
