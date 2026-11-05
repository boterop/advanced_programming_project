package com.project.advanced.project.application.dto.response

import java.math.BigDecimal

data class RegistrarOcupanteResponse(
    val ocupanteId: String,
    val valorReserva: BigDecimal,/** Valor de la reserva tras recalcularlo con el nuevo ocupante. */
    val ajuste: BigDecimal,/** Valor de la reserva tras recalcularlo con el nuevo ocupante. */
    val totalOcupantes: Int
)