package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.MotivoBloqueo
import java.time.LocalDate

data class BloquearApartamentoCommand(
    val apartamentoId: String,
    val fechaInicio: LocalDate, /** Primera noche del bloqueo. */
    val fechaFin: LocalDate, /** Fecha de fin: la noche de esta fecha no se bloquea (intervalo [inicio, fin)). */
    val motivo: MotivoBloqueo,
    val observacion: String = ""
)
