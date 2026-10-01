package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo

interface ReservaRepository {
    fun listar(): List<Reserva>

    fun buscar(id: ID): Reserva?

    fun buscarActivasPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Reserva>

    fun guardar(reserva: Reserva)

    fun eliminar(reserva: Reserva)
}
