package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.RegistrarLlegadaRequest
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.RegistroEstanciaService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
@Transactional
class RegistrarLlegadaUseCase(
    private val reservaRepository: ReservaRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val registroEstanciaService: RegistroEstanciaService
) {
    fun ejecutar(
        comando: RegistrarLlegadaRequest,
        hoy: LocalDate = LocalDate.now(),
    ) {
        val codigo = CodigoReserva(comando.codigo)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        // El estado operativo vigente vive en el repositorio de apartamentos, no en la copia de la reserva.
        val apartamento =
            apartamentoRepository.buscar(reserva.apartamento.id)
                ?: throw ReglaDominioException("No existe el apartamento con id ${reserva.apartamento.id.valor}")

        registroEstanciaService.registrarLlegada(reserva, apartamento, hoy)

        reservaRepository.guardar(reserva)
        apartamentoRepository.guardar(apartamento)
    }
}