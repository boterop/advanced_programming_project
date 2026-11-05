package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.CerrarFolioRequest
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.GestionFolioService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CerrarFolioUseCase(
    private val reservaRepository: ReservaRepository,
    private val folioRepository: FolioRepository,
    private val gestionFolioService: GestionFolioService
) {
    fun ejecutar(comando: CerrarFolioRequest) {
        val codigo = CodigoReserva(comando.codigoReserva)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        val folio =
            folioRepository.buscar(reserva.id)
                ?: throw ReglaDominioException("La reserva ${reserva.codigo.valor} no tiene folio")

        // RN-17: sin saldo en cero no se cierra. La autorizacion del administrador queda fuera de este caso de uso.
        gestionFolioService.cerrar(folio)

        folioRepository.actualizar(folio)
    }
}