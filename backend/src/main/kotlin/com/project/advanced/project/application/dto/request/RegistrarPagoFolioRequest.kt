package com.project.advanced.project.application.dto.request

import com.project.advanced.project.domain.valueobject.MedioPago
import java.math.BigDecimal

data class RegistrarPagoFolioRequest(
    val codigoReserva: String,
    val medioPago: MedioPago,
    val valor: BigDecimal
)