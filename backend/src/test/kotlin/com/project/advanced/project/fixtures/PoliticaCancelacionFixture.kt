package com.project.advanced.project.fixtures

import com.project.advanced.project.domain.entity.PoliticaCancelacion
import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.VersionPolitica
import java.time.LocalDate
import java.util.UUID

object PoliticaCancelacionFixture {
    operator fun invoke(
        id: ID = ID(UUID.randomUUID().toString()),
        version: VersionPolitica = VersionPolitica(1, LocalDate.now()),
        descripcion: Descripcion = Descripcion("Politica"),
    ) = PoliticaCancelacion.crear(id, version, descripcion)
}
