package com.project.advanced.project.application.dto.request

import java.time.LocalDate

data class ConsultarDisponibilidadRequest(
    val fechaEntrada: LocalDate,
    val fechaSalida: LocalDate,
    val cantidadOcupantes: Int,
    val pagina: Int = 1
)