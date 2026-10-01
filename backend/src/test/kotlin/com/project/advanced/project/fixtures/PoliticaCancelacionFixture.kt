package com.project.advanced.project.fixtures

import com.project.advanced.project.domain.entity.PoliticaCancelacion
import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.VersionPolitica
import java.time.LocalDate

object PoliticaCancelacionFixture {
    operator fun invoke(
        version: VersionPolitica = VersionPolitica(1, LocalDate.now()),
        descripcion: Descripcion = Descripcion("Politica"),
    ) = PoliticaCancelacion.crear(version, descripcion)
}
