package com.project.advanced.project.application.dto.request

import java.time.LocalDate
import java.time.LocalTime

data class ModificarReservaRequest(
    val codigo: String,
    val apartamentoId: String? = null,
    val fechaEntrada: LocalDate? = null,
    val fechaSalida: LocalDate? = null,
    val horaLlegada: LocalTime? = null,
    val otrosOcupantes: List<OcupanteRequest>? = null,
)