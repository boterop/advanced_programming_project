package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.Periodo

data class Estancia(
    val periodo: Periodo,
) {
    fun calcularValor(
        ocupantes: List<Ocupante>,
        tarifa: Double,
    ): Double {
        val facturables = ocupantes.count { it.esFacturable(this) }

        return tarifa * facturables * periodo.noches()
    }
}
