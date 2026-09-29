package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.Tarifa

data class Estancia(
    val periodo: Periodo,
) {
    fun calcularValor(
        ocupantes: List<Ocupante>,
        tarifa: Tarifa,
    ): Dinero {
        val cantidadFacturables = ocupantes.count { it.esFacturable(this) }

        return tarifa.valor.multiplicar(cantidadFacturables).multiplicar(periodo.noches())
    }
}
