package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.RegistrarSalidaRequest
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.RegistroEstanciaService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class RegistrarSalidaUseCase(
    private val reservaRepository: ReservaRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val folioRepository: FolioRepository,
    private val registroEstanciaService: RegistroEstanciaService
) {
    fun ejecutar(comando: RegistrarSalidaRequest) {
        val codigo = CodigoReserva(comando.codigo)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        val apartamento =
            apartamentoRepository.buscar(reserva.apartamento.id)
                ?: throw ReglaDominioException("No existe el apartamento con id ${reserva.apartamento.id.valor}")

        val folio =
            folioRepository.buscar(reserva.id)
                ?: throw ReglaDominioException("La reserva ${reserva.codigo.valor} no tiene folio")

        registroEstanciaService.registrarSalida(reserva, apartamento, folio)

        reservaRepository.guardar(reserva)
        apartamentoRepository.guardar(apartamento)
        folioRepository.actualizar(folio)
    }
}