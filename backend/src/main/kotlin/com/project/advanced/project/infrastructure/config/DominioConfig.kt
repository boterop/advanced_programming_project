package com.project.advanced.project.infrastructure.config

import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.PagoRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.BloqueoApartamentoService
import com.project.advanced.project.domain.service.BusquedaDisponibilidadService
import com.project.advanced.project.domain.service.CatalogoApartamentoService
import com.project.advanced.project.domain.service.CotizarEstanciaService
import com.project.advanced.project.domain.service.DisponibilidadApartamentoService
import com.project.advanced.project.domain.service.DisponibilidadModificacionService
import com.project.advanced.project.domain.service.EstadoOperativoApartamentoService
import com.project.advanced.project.domain.service.GestionFolioService
import com.project.advanced.project.domain.service.PagarFolioService
import com.project.advanced.project.domain.service.RegistroEstanciaService
import com.project.advanced.project.domain.service.RegistroOcupanteService
import com.project.advanced.project.domain.service.VencimientoReservaService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/** Expone como beans los servicios de dominio, que se mantienen libres de anotaciones de Spring. */
@Configuration
class DominioConfig {
    @Bean
    fun disponibilidadApartamentoService(
        reservaRepository: ReservaRepository,
        bloqueoRepository: BloqueoRepository
    ): DisponibilidadApartamentoService = DisponibilidadApartamentoService(reservaRepository, bloqueoRepository)

    @Bean
    fun cotizarEstanciaService(): CotizarEstanciaService = CotizarEstanciaService()

    @Bean
    fun disponibilidadModificacionService(
        reservaRepository: ReservaRepository,
        bloqueoRepository: BloqueoRepository
    ): DisponibilidadModificacionService = DisponibilidadModificacionService(reservaRepository, bloqueoRepository)

    @Bean
    fun registroEstanciaService(): RegistroEstanciaService = RegistroEstanciaService()

    @Bean
    fun vencimientoReservaService(): VencimientoReservaService = VencimientoReservaService()

    @Bean
    fun busquedaDisponibilidadService(
        apartamentoRepository: ApartamentoRepository,
        disponibilidadApartamentoService: DisponibilidadApartamentoService
    ): BusquedaDisponibilidadService = BusquedaDisponibilidadService(apartamentoRepository,
        disponibilidadApartamentoService)

    @Bean
    fun bloqueoApartamentoService(reservaRepository: ReservaRepository):
            BloqueoApartamentoService = BloqueoApartamentoService(reservaRepository)

    @Bean
    fun estadoOperativoApartamentoService(): EstadoOperativoApartamentoService =
        EstadoOperativoApartamentoService()

    @Bean
    fun catalogoApartamentoService(apartamentoRepository: ApartamentoRepository):
            CatalogoApartamentoService = CatalogoApartamentoService(apartamentoRepository)

    @Bean
    fun gestionFolioService(): GestionFolioService = GestionFolioService()

    @Bean
    fun pagarFolioService(
        pagoRepository: PagoRepository,
        folioRepository: FolioRepository
    ): PagarFolioService = PagarFolioService(pagoRepository, folioRepository)

    @Bean
    fun registroOcupanteService(cotizarEstanciaService: CotizarEstanciaService): RegistroOcupanteService =
        RegistroOcupanteService(cotizarEstanciaService)
}