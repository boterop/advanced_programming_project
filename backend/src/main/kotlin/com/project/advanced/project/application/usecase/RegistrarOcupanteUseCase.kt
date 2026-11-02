package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.RegistrarOcupanteCommand
import com.project.advanced.project.application.dto.RegistrarOcupanteResult
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.RegistroOcupanteService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class RegistrarOcupanteUseCase(
    private val reservaRepository: ReservaRepository,
    private val folioRepository: FolioRepository,
    private val registroOcupanteService: RegistroOcupanteService
) {
    fun ejecutar(comando: RegistrarOcupanteCommand): RegistrarOcupanteResult {
        val codigo = CodigoReserva(comando.codigoReserva)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        // El folio de una reserva comparte su id.
        val folio =
            folioRepository.buscar(reserva.id)
                ?: throw ReglaDominioException("La reserva ${reserva.codigo.valor} no tiene folio")

        val ocupante = comando.ocupante.aDominio()

        val ajuste = registroOcupanteService.registrar(reserva, folio, ocupante, ID(UUID.randomUUID().toString()))

        reservaRepository.guardar(reserva)
        folioRepository.actualizar(folio)

        return RegistrarOcupanteResult(
            ocupanteId = ocupante.id.valor,
            valorReserva = reserva.valor.cantidad(),
            ajuste = ajuste.cantidad(),
            totalOcupantes = reserva.ocupantes.size
        )
    }
}