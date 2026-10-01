package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class Descripcion(
    val descripcion: String,
) {
    init {
        if (descripcion.isBlank()) throw ReglaDominioException("La descripcion no puede ser vacía")
        if (descripcion.length > 255) throw ReglaDominioException("La descripcion no puede tener mas de 255 caracteres")
    }
}
