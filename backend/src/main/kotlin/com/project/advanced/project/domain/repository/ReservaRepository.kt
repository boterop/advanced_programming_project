package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo

interface ReservaRepository {
    fun buscarActivasPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Reserva>
}
