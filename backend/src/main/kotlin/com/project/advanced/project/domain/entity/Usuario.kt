package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Contrasena
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad

class Usuario private constructor(
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
            documento: DocumentoIdentidad,
            password: Contrasena,
            ocupante: Ocupante,
        ): Usuario = Usuario(documento, password, ocupante)
    }
}
