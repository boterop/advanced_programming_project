package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.time.LocalTime

data class HoraLlegada(
    val hora: LocalTime,
) {
    init {
        if (hora == null) throw ReglaDominioException("La hora no puede ser nula")
    }
}
