package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.time.LocalDate
import java.time.Period

data class FechaNacimiento(
    val valor: LocalDate,
) {
    init {
        if (valor.isAfter(LocalDate.now())) {
            throw ReglaDominioException("La fecha de nacimiento no es válida")
        }
    }

    fun calcularEdad(fecha: LocalDate): Int = Period.between(valor, fecha).years
}
