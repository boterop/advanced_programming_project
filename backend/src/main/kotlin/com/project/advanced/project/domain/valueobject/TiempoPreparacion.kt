package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class TiempoPreparacion(
    val horas: Int,
) {
    init {
        if (horas < 0) {
            throw ReglaDominioException("El tiempo de preparacion no puede ser negativo")
        }
    }
}
