package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.Periodo

interface BloqueoRepository {
    fun buscarVigentesPorApartamento(
        apartamentoId: IdApartamento,
        periodo: Periodo,
    ): List<Bloqueo>
}
