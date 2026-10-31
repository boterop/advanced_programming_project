package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.ConsultarReservaPorCodigoQuery
import com.project.advanced.project.application.dto.ReservaDetalleResult
import com.project.advanced.project.application.dto.aDetalle
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ConsultarReservaPorCodigoUseCase(
    private val reservaRepository: ReservaRepository,
    private val folioRepository: FolioRepository
) {
    fun ejecutar(consulta: ConsultarReservaPorCodigoQuery): ReservaDetalleResult {
        val codigo = CodigoReserva(consulta.codigo)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        val saldo = folioRepository.buscar(reserva.id)?.saldo()?.cantidad()

        return reserva.aDetalle(saldo)
    }
}