package com.project.advanced.project.application.dto

import java.time.LocalDate
import java.time.LocalTime

data class ModificarReservaCommand(
    val codigo: String,
    val apartamentoId: String? = null,
    val fechaEntrada: LocalDate? = null,
    val fechaSalida: LocalDate? = null,
    val horaLlegada: LocalTime? = null,
    val otrosOcupantes: List<OcupanteCommand>? = null,
)
