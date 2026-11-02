package com.project.advanced.project.application.dto

import java.math.BigDecimal

data class RegistrarOcupanteResult(
    val ocupanteId: String,
    val valorReserva: BigDecimal, /** Valor de la reserva tras recalcularlo con el nuevo ocupante. */
    val ajuste: BigDecimal, /** Valor de la reserva tras recalcularlo con el nuevo ocupante. */
    val totalOcupantes: Int
)
