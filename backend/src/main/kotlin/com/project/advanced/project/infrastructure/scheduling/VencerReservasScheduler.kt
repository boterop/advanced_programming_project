package com.project.advanced.project.infrastructure.scheduling

import com.project.advanced.project.application.usecase.VencerReservasUseCase
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Vence una reserva sin intervencion manual (RN-21)
 */
@Component
class VencerReservasScheduler(
    private val vencerReservasUseCase: VencerReservasUseCase
) {
    @Scheduled(cron = "\${sga.reserva.vencimiento-cron:0 0 * * * *}")
    fun ejecutar() {
        val vencidas = vencerReservasUseCase.ejecutar()

        if (vencidas > 0) logger.info("Reservas pendientes canceladas por vencimiento: {}", vencidas)
    }

    private companion object {
        val logger = LoggerFactory.getLogger(VencerReservasScheduler::class.java)
    }
}