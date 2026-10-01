package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class CodigoReserva(
    var valor: String,
) {
    init {
        if (valor.isBlank() || valor.isEmpty()) throw ReglaDominioException("El codigo no puede ser nulo")
        valor = valor.trim()
    }
}
