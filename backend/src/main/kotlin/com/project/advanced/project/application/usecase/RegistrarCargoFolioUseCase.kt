package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.RegistrarCargoFolioCommand
import com.project.advanced.project.application.dto.RegistrarCargoFolioResult
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.GestionFolioService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class RegistrarCargoFolioUseCase(
    private val reservaRepository: ReservaRepository,
    private val folioRepository: FolioRepository,
    private val gestionFolioService: GestionFolioService
) {
    fun ejecutar(comando: RegistrarCargoFolioCommand): RegistrarCargoFolioResult {
        val codigo = CodigoReserva(comando.codigoReserva)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        val folio =
            folioRepository.buscar(reserva.id)
                ?: throw ReglaDominioException("La reserva ${reserva.codigo.valor} no tiene folio")

        val cargo =
            gestionFolioService.registrarCargo(
                folio,
                ID(UUID.randomUUID().toString()),
                comando.concepto,
                Dinero(comando.valor),
            )

        folioRepository.guardar(folio)
        return RegistrarCargoFolioResult(cargo.id.valor, folio.saldo().cantidad())
    }
}