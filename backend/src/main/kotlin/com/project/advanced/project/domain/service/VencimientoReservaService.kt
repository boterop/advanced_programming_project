package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.EstadoReserva
import java.time.LocalDate

/**
 * Decide si una reserva con estado PENDIENTE superó el plazo para cambiar su estado a CONFIRMADA
 * (RN-21)
 */
class VencimientoReservaService {
    fun estaVencida(
        reserva: Reserva,
        hoy: LocalDate,
        plazoConfirmacionDias: Long,
    ): Boolean =
        reserva.estado == EstadoReserva.PENDIENTE &&
                reserva.fechaCreacion.valor
                    .plusDays(plazoConfirmacionDias)
                    .isBefore(hoy)
}