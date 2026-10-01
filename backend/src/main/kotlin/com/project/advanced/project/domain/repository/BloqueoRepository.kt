package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo

interface BloqueoRepository {
    fun buscarVigentesPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Bloqueo>
}
