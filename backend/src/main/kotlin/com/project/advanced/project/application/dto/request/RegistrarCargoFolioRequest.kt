package com.project.advanced.project.application.dto.request

import com.project.advanced.project.domain.valueobject.ConceptoCargo
import java.math.BigDecimal

data class RegistrarCargoFolioRequest(
    val codigoReserva: String,
    val concepto: ConceptoCargo,/** Solo SERVICIO o PENALIDAD. */
    val valor: BigDecimal
)