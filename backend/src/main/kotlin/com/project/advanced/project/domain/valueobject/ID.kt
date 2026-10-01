package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.util.UUID

data class ID(
    var id: String,
) {
    init {
        id = id.trim()
        if (id.isBlank() || id.isEmpty()) {
            throw ReglaDominioException("El ID no es valido")
        }
        try {
            UUID.fromString(id)
        } catch (e: Exception) {
            throw ReglaDominioException("El ID no es valido")
        }
    }
}
