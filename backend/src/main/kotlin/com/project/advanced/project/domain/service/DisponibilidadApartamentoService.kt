package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.exception.ReglaDominioException

class DisponibilidadApartamentoService(
    val reservaRepository: ReservaRepository,
    val bloqueoRepository: BloqueoRepository,
) {
    fun verificarDisponibilidad(
        apartamento: Apartamento,
        estancia: Estancia,
        totalOcupantes: Int,
    ): void {
        if (!apartamento.admite(totalOcupantes)) throw ReglaDominioException("El apartamento no admite $totalOcupantes ocupantes")
        if (!apartamento.esActivo()) throw ReglaDominioException("El apartamento no esta activo")

        reservaRepository
            .buscarActivasPorApartamento(apartamento.getId())
            .forEach {
                if (it.estancia.solapa(estancia)) throw ReglaDominioException("Ya existe una reserva para la misma fecha")
            }

        bloqueoRepository
            .buscarActivosPorApartamento(apartamento.getId())
            .forEach {
                if (estancia.solapa(
                        it.fechaInicio,
                        it.fechaFinal,
                    )
                ) {
                    throw ReglaDominioException("El apartamento esta bloqueado para la misma fecha")
                }
            }
    }
}
