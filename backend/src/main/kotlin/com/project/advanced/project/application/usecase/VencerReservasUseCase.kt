package com.project.advanced.project.application.usecase

import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.VencimientoReservaService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

/**
 * Vence las reservas de estado PENDIENTE que no se confirmen durante el plazo (RN-21)
 * La reserva cambia a estado CANCELADA y libera la disponibilidad y se activa gracias al adaptador
 * programado de infraestructura
 */
@Service
@Transactional
class VencerReservasUseCase(
    private val reservaRepository: ReservaRepository,
    private val vencimientoReservaService: VencimientoReservaService
) {
    /** Devuelve la cantidad de reservas canceladas por vencimiento. */
    fun ejecutar(hoy: LocalDate = LocalDate.now()): Int {
        val plazo = reservaRepository.plazoConfirmacionDias()

        val vencidas =
            reservaRepository
                .listar()
                .filter { vencimientoReservaService.estaVencida(it, hoy, plazo) }

        vencidas.forEach { reserva ->
            reserva.cancelar(MOTIVO_VENCIMIENTO)
            reservaRepository.guardar(reserva)
        }

        return vencidas.size
    }

    private companion object {
        const val MOTIVO_VENCIMIENTO = "Vencimiento del plazo de confirmacion"
    }
}