package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.ConfirmarReservaCommand
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ConfirmarReservaUseCase(
    private val reservaRepository: ReservaRepository,
) {
    fun ejecutar(comando: ConfirmarReservaCommand) {
        val codigo = CodigoReserva(comando.codigo)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        reserva.confirmar()
        reservaRepository.guardar(reserva)
    }
}
