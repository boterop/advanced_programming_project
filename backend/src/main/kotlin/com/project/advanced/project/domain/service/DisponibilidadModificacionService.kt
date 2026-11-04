package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.TiempoPreparacion

class DisponibilidadModificacionService(
    private val reservaRepository: ReservaRepository,
    private val bloqueoRepository: BloqueoRepository,
) {
    fun verificarDisponibilidad(
        reservaId: ID,
        apartamentoId: ID,
        estancia: Estancia,
        tiempoPreparacion: TiempoPreparacion,
    ) {
        DisponibilidadApartamentoService(ReservasSin(reservaRepository, reservaId), bloqueoRepository)
            .verificarDisponibilidad(apartamentoId, estancia, tiempoPreparacion)
    }

    private class ReservasSin(
        private val delegado: ReservaRepository,
        private val ignorada: ID,
    ) : ReservaRepository by delegado {
        override fun buscarActivasPorApartamento(
            apartamentoId: ID,
            periodo: Periodo,
        ): List<Reserva> = delegado.buscarActivasPorApartamento(apartamentoId, periodo).filter { it.id != ignorada }
    }
}