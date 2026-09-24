package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class IdentificacionApartamento(
    val valor: String
) {
    companion object {
        operator fun invoke(valor: String?): IdentificacionApartamento {
            if (valor.isNullOrBlank()) {
                throw ReglaDominioException("La identificación del apartamento es obligatoria")
            }
            return IdentificacionApartamento(valor.trim().uppercase())
        }
    }
}