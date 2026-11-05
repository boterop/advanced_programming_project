package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.CancelarReservaRequest
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class CancelarReservaUseCase(
    private val reservaRepository: ReservaRepository,
) {
    fun ejecutar(comando: CancelarReservaRequest) {
        val codigo = CodigoReserva(comando.codigo)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        // La politica aplicable es la congelada en la propia reserva (RN-13), no la vigente.
        reserva.cancelar(comando.motivo)

        reservaRepository.guardar(reserva)
    }
}