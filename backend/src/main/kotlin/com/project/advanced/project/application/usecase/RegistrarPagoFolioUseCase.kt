package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.RegistrarPagoFolioCommand
import com.project.advanced.project.application.dto.RegistrarPagoFolioResult
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.GestionFolioService
import com.project.advanced.project.domain.service.PagarFolioService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegistrarPagoFolioUseCase(
    private val reservaRepository: ReservaRepository,
    private val folioRepository: FolioRepository,
    private val gestionFolioService: GestionFolioService,
    private val pagarFolioService: PagarFolioService
) {
    fun ejecutar(comando: RegistrarPagoFolioCommand): RegistrarPagoFolioResult {
        val codigo = CodigoReserva(comando.codigoReserva)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        val folio =
            folioRepository.buscar(reserva.id)
                ?: throw ReglaDominioException("La reserva ${reserva.codigo.valor} no tiene folio")

        val valor = Dinero(comando.valor)
        gestionFolioService.verificarPagable(folio, valor)

        // Crea el pago, valida que no supere el saldo, cierra el folio si queda en cero y persiste ambos.
        pagarFolioService.pagar(reserva.id, comando.medioPago, valor)

        val actualizado = folioRepository.buscar(reserva.id) ?: folio

        return RegistrarPagoFolioResult(
            saldoPendiente = actualizado.saldo().cantidad(),
            folioCerrado = actualizado.estadoActual() == EstadoFolio.CERRADO
        )
    }
}