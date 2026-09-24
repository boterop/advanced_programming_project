package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class Tarifa(
    val temporada: IdTemporada,
    val valor: Double,
) {
    init {
        if (temporada == null) throw ReglaDominioException("La temporada no puede ser nula")
        if (valor < 0) throw ReglaDominioException("El valor no puede ser negativo")
    }
}
