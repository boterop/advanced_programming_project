package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class Contrasena(
    val value: String,
) {
    init {
        if (value.isBlank()) throw ReglaDominioException("La contraseña no puede ser vacía")
        if (value.length < 8) throw ReglaDominioException("La contraseña debe tener al menos 8 caracteres")
        if (value.any { it.isWhitespace() }) throw ReglaDominioException("La contraseña no puede contener espacios en blanco")
        if (!value.any { it.isUpperCase() }) throw ReglaDominioException("La contraseña debe contener al menos una mayúscula")
        if (!value.any { !it.isLetterOrDigit() }) throw ReglaDominioException("La contraseña debe contener al menos un carácter numérico")
    }
}
