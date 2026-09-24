package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.time.LocalDate

data class VersionPolitica(
    val version: Int,
    val fecha: LocalDate,
) {
    init {
        if (version < 0) throw ReglaDominioException("La version no puede ser negativa")
        if (fecha == null) throw ReglaDominioException("La fecha no puede ser nula")
    }
}
