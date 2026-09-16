package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class CorreoElectronico(
    val valor: String,
) {
    init {
        require(
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(valor),
        ) {
            throw ReglaDominioException("El correo electrónico no es válido")
        }
    }
}
