package com.project.advanced.project.application.dto.response

import java.math.BigDecimal

data class CrearReservaResponse(
    val id: String,
    val codigo: String,
    val valor: BigDecimal,
)