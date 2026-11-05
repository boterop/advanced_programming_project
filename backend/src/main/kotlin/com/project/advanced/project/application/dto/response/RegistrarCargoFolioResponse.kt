package com.project.advanced.project.application.dto.response

import java.math.BigDecimal

data class RegistrarCargoFolioResponse(
    val cargoId: String,
    val saldoPendiente: BigDecimal
)