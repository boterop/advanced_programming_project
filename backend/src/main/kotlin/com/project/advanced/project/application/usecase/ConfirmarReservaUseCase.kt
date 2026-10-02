package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva

class ConfirmarReservaUseCase(
    private val reservaRepository: ReservaRepository,
) {
    fun ejecutar(codigo: CodigoReserva) {
        val reserva = reservaRepository.buscarPorCodigo(codigo)

        if (reserva == null) throw ReservaNoEncontradaException(codigo)

        reserva.confirmar()
        reservaRepository.guardar(reserva)
    }
}
