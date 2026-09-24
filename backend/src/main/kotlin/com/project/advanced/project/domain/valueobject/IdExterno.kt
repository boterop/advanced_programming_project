package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.CanalOrigen

data class IdExterno(
    val canalOrigen: CanalOrigen,
    var valor: String,
) {
    init {
        if (canalOrigen == null) throw ReglaDominioException("El canalOrigen no puede ser nulo")
        if (valor.isBlank() || valor.isEmpty()) throw ReglaDominioException("El valor no puede ser nulo")

        valor = valor.trim()
    }
}
