package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class Capacidad(
    var valor: Int,
) {
    init {
        if (valor < 0) throw ReglaDominioException("El valor no puede ser negativo")
    }
}
