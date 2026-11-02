package com.project.advanced.project.application.dto

import java.math.BigDecimal

data class RegistrarCargoFolioResult(
    val cargoId: String,
    val saldoPendiente: BigDecimal
)
