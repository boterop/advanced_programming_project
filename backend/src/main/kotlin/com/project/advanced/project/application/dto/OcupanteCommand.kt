package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.FechaNacimiento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import java.time.LocalDate
import java.util.UUID

data class OcupanteCommand(
    val documentoIdentidad: String,
    val nombre: String,
    val correoElectronico: String,
    val fechaNacimiento: LocalDate,
) {
    fun aDominio(): Ocupante =
        Ocupante.crear(
            ID(UUID.randomUUID().toString()),
            DocumentoIdentidad(documentoIdentidad),
            Nombre(nombre),
            CorreoElectronico(correoElectronico),
            FechaNacimiento(fechaNacimiento),
        )
}
