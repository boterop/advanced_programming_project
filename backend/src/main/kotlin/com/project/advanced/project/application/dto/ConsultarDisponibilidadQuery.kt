package com.project.advanced.project.application.dto

import java.time.LocalDate

data class ConsultarDisponibilidadQuery(
    val fechaEntrada: LocalDate,
    val fechaSalida: LocalDate,
    val cantidadOcupantes: Int,
    val pagina: Int = 1
)
