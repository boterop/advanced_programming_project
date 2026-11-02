package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MotivoBloqueo
import com.project.advanced.project.domain.valueobject.Periodo

class BloqueoApartamentoService(
    private val reservaRepository: ReservaRepository,
) {
    /** No puede bloquearse una noche que ya tenga reservas activas. */
    fun bloquear(
        id: ID,
        apartamento: Apartamento,
        periodo: Periodo,
        motivo: MotivoBloqueo,
        observacion: String,
    ): Bloqueo {
        if (reservaRepository.buscarActivasPorApartamento(apartamento.id, periodo).isNotEmpty()) {
            throw ReglaDominioException("No se puede bloquear: el apartamento tiene reservas activas en el periodo")
        }
        return Bloqueo.crear(id, apartamento.id, periodo, motivo, observacion.trim())
    }

    fun desbloquear(bloqueo: Bloqueo) {
        if (!bloqueo.vigente) throw ReglaDominioException("El bloqueo ya no esta vigente")
        bloqueo.levantar()
    }
}