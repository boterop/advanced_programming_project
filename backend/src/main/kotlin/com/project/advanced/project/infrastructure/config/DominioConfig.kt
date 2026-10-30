package com.project.advanced.project.infrastructure.config

import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.CotizarEstanciaService
import com.project.advanced.project.domain.service.DisponibilidadApartamentoService
import com.project.advanced.project.domain.service.DisponibilidadModificacionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/** Expone como beans los servicios de dominio, que se mantienen libres de anotaciones de Spring. */
@Configuration
class DominioConfig {
    @Bean
    fun disponibilidadApartamentoService(
        reservaRepository: ReservaRepository,
        bloqueoRepository: BloqueoRepository,
    ): DisponibilidadApartamentoService = DisponibilidadApartamentoService(reservaRepository, bloqueoRepository)

    @Bean
    fun cotizarEstanciaService(): CotizarEstanciaService = CotizarEstanciaService()

    @Bean
    fun disponibilidadModificacionService(
        reservaRepository: ReservaRepository,
        bloqueoRepository: BloqueoRepository,
    ): DisponibilidadModificacionService = DisponibilidadModificacionService(reservaRepository, bloqueoRepository)
}