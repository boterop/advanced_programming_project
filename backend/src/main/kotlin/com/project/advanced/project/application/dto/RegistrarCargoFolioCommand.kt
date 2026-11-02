package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.ConceptoCargo
import java.math.BigDecimal

data class RegistrarCargoFolioCommand(
    val codigoReserva: String,
    val concepto: ConceptoCargo, /** Solo SERVICIO o PENALIDAD. */
    val valor: BigDecimal
)
