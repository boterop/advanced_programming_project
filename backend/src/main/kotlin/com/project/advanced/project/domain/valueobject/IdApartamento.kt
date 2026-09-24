package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class IdApartamento(
    var id: String,
) {
    init {
        if (id.isBlank() || id.isEmpty()) {
            throw ReglaDominioException("El IdApartamento no es valido")
        }
        id = id.trim().uppercase()
    }
}
