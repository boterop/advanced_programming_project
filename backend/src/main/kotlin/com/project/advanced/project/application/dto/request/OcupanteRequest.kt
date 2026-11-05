package com.project.advanced.project.application.dto.request

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.FechaNacimiento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import java.time.LocalDate
import java.util.UUID

data class OcupanteRequest(
    val documentoIdentidad: String,
    val nombre: String,
    val correoElectronico: String,
    val fechaNacimiento: LocalDate,
) {
    fun aDominio(): Ocupante =
        Ocupante.Companion.crear(
            ID(UUID.randomUUID().toString()),
            DocumentoIdentidad(documentoIdentidad),
            Nombre(nombre),
            CorreoElectronico(correoElectronico),
            FechaNacimiento(fechaNacimiento),
        )
}