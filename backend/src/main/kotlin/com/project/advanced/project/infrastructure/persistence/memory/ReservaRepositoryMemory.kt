package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.Periodo

class ReservaRepositoryMemory : ReservaRepository {
    private val reservas: HashMap<String, Reserva> = HashMap()

    override fun buscarActivasPorApartamento(
        apartamentoId: IdApartamento,
        periodo: Periodo,
    ): List<Reserva> = reservas.values.filter { it.apartamento.id == apartamentoId && it.estancia.periodo.solapa(periodo) }.toList()
}
