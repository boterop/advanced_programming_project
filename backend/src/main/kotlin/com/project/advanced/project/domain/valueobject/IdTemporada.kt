package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class IdTemporada(
    var id: String,
) {
    init {
        if (id.isBlank() || id.isEmpty()) {
            throw ReglaDominioException("El id de la temporada no es valido")
        }
        id = id.trim()
    }
}
