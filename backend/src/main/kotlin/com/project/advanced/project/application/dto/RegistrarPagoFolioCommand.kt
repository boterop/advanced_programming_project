package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.MedioPago
import java.math.BigDecimal

data class RegistrarPagoFolioCommand(
    val codigoReserva: String,
    val medioPago: MedioPago,
    val valor: BigDecimal
)
