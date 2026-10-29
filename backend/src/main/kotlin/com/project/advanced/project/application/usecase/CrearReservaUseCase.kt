package com.project.advanced.project.application.usecase

import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.PoliticaCancelacionRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.CotizarEstanciaService
import com.project.advanced.project.domain.service.DisponibilidadApartamentoService
import org.springframework.stereotype.Service


@Service
class CrearReservaUseCase(
    private val reservaRepository: ReservaRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val politicaCancelacionRepository: PoliticaCancelacionRepository,
    private val disponibilidadApartamentoService: DisponibilidadApartamentoService,
    private val cotizarEstanciaService: CotizarEstanciaService,
)  {
}