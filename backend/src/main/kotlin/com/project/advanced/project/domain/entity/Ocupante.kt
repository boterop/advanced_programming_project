package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import java.time.LocalDate

class Ocupante private constructor(
    val documento: DocumentoIdentidad,
    var nombre: String,
    val fechaNacimiento: LocalDate,
) {
    init {
        if (documento == null) throw ReglaDominioException("El documento no puede ser nulo")
        if (nombre == null) throw ReglaDominioException("El nombre no puede ser nulo")
        if (fechaNacimiento == null) throw ReglaDominioException("La fecha de nacimiento no puede ser nula")

        nombre = nombre.trim()
    }
}
