package com.project.advanced.project.fixtures

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.FechaNacimiento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import java.time.LocalDate
import java.util.UUID

object OcupanteFixture {
    operator fun invoke(
        id: String = UUID.randomUUID().toString(),
        documento: String = "123456789",
        nombre: String = "Jhon Doe",
        correo: String = "jhon.doe@gmail.com",
        fechaNacimiento: LocalDate = LocalDate.of(1990, 1, 1),
    ) = Ocupante.crear(
        ID(id),
        DocumentoIdentidad(documento),
        Nombre(nombre),
        CorreoElectronico(correo),
        FechaNacimiento(fechaNacimiento),
    )
}
