package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.util.UUID

data class ID(
    var valor: String,
) {
    init {
        valor = valor.trim()
        if (valor.isBlank() || valor.isEmpty()) {
            throw ReglaDominioException("El ID no es valido")
        }
        try {
            UUID.fromString(valor)
        } catch (e: Exception) {
            throw ReglaDominioException("El ID no es valido")
        }
    }

    override fun hashCode(): Int = valor.hashCode()

    override fun equals(other: Any?): Boolean = other is ID && valor == other.valor
}
