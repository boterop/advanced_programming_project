package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.time.LocalDate

data class FechaCreacion(
    val valor: LocalDate,
) {
    init {
        if (valor.isAfter(LocalDate.now())) {
            throw ReglaDominioException("La fecha de creación no es válida")
        }
    }
}
