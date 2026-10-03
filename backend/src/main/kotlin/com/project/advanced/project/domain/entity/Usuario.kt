package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Contrasena
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.ID

class Usuario private constructor(
    val id: ID,
    val documento: DocumentoIdentidad,
    val password: Contrasena,
    val ocupante: Ocupante,
) {
    init {
        if (documento == null) throw ReglaDominioException("El documento no puede ser nulo")
        if (password == null) throw ReglaDominioException("La contraseña no puede ser nula")
        if (ocupante == null) throw ReglaDominioException("El ocupante no puede ser nulo")
    }

    companion object {
        fun crear(
            id: ID,
            documento: DocumentoIdentidad,
            password: Contrasena,
            ocupante: Ocupante,
        ): Usuario = Usuario(id, documento, password, ocupante)
    }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Usuario && id == other.id
}
