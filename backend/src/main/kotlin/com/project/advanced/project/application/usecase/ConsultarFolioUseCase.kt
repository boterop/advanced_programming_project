package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.ConsultarFolioQuery
import com.project.advanced.project.application.dto.FolioDetalleResult
import com.project.advanced.project.application.dto.aDetalle
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ConsultarFolioUseCase(
    private val reservaRepository: ReservaRepository,
    private val folioRepository: FolioRepository
) {
    fun ejecutar(consulta: ConsultarFolioQuery): FolioDetalleResult {
        val codigo = CodigoReserva(consulta.codigoReserva)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        // El folio de una reserva comparte su id (un folio pertenece a una sola reserva).
        val folio =
            folioRepository.buscar(reserva.id)
                ?: throw ReglaDominioException("La reserva ${reserva.codigo.valor} no tiene folio")

        return folio.aDetalle(reserva.codigo.valor)
    }
}