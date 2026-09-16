package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.FechaNacimiento
import com.project.advanced.project.domain.valueobject.Nombre

class Ocupante private constructor(
    val documentoIdentidad: DocumentoIdentidad,
    val nombre: Nombre,
    val correoElectronico: CorreoElectronico,
    val fechaNacimiento: FechaNacimiento,
) {
    init {
        if (documentoIdentidad == null) {
            throw ReglaDominioException("El documento de identidad no es valido")
        }
        if (nombre == null) {
            throw ReglaDominioException("El nombre no puede ser vacío")
        }
        if (correoElectronico == null) {
            throw ReglaDominioException("El correo electrónico no puede ser vacío")
        }
        if (fechaNacimiento == null) {
            throw ReglaDominioException("La fecha de nacimiento no puede ser vacía")
        }
    }

    companion object {
        fun crear(
            documentoIdentidad: DocumentoIdentidad,
            nombre: Nombre,
            correoElectronico: CorreoElectronico,
            fechaNacimiento: FechaNacimiento,
        ): Ocupante = Ocupante(documentoIdentidad, nombre, correoElectronico, fechaNacimiento)
    }

    fun esFacturable(estancia: Estancia): Boolean =
        fechaNacimiento.calcularEdad(estancia.entrada) >= 18 // TODO: Este valor debería ser un parametro
}
