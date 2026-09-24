package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class DocumentoIdentidad(
    var numero: String,
) {
    init {
        if (numero.isBlank() || numero.isEmpty()) {
            throw ReglaDominioException("El documento de identidad no es valido")
        }
        numero = numero.trim()
        if (!numero.chars().allMatch(Character::isLetterOrDigit)) {
            throw ReglaDominioException("El documento de identidad solo admite numeros")
        }
    }
}
