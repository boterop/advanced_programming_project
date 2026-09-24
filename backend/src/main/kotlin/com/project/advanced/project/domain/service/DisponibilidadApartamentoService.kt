package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.TiempoPreparacion

class DisponibilidadApartamentoService(
    val reservaRepository: ReservaRepository,
    val bloqueoRepository: BloqueoRepository,
) {
    fun verificarDisponibilidad(
        apartamentoId: IdApartamento,
        estancia: Estancia,
        tiempoPreparacion: TiempoPreparacion,
    ) {
        reservaRepository
            .buscarActivasPorApartamento(apartamentoId, estancia.periodo)
            .forEach { reserva: Reserva ->
                if (estancia.periodo.solapa(
                        reserva.estancia.periodo,
                    )
                ) {
                    throw ReglaDominioException("Ya existe una reserva para la misma fecha")
                }
            }

        bloqueoRepository
            .buscarVigentesPorApartamento(apartamentoId, estancia.periodo)
            .forEach { bloqueo: Bloqueo ->
                if (estancia.periodo.solapa(bloqueo.periodo)) {
                    throw ReglaDominioException("El apartamento esta bloqueado para la misma fecha")
                }
            }
    }
}
