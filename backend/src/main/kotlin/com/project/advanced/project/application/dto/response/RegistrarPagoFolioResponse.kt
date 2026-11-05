package com.project.advanced.project.application.dto.response

import java.math.BigDecimal

data class RegistrarPagoFolioResponse(
    val saldoPendiente: BigDecimal,
    val folioCerrado: Boolean /** True si el pago dejo el saldo en cero y el folio quedo cerrado. */
)