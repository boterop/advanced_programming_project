package com.project.advanced.project.fixtures

import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MotivoBloqueo
import com.project.advanced.project.domain.valueobject.Periodo
import java.time.LocalDate
import java.util.UUID

object BloqueoFixture {
    val periodo = Periodo(LocalDate.now(), LocalDate.now().plusDays(3))

    operator fun invoke(
        apartamentoId: ID = ID(UUID.randomUUID().toString()),
        periodo: Periodo = BloqueoFixture.periodo,
        motivo: MotivoBloqueo = MotivoBloqueo.USO_INTERNO,
        observacion: String = "Uso interno",
    ) = Bloqueo.crear(apartamentoId, periodo, motivo, observacion)
}
