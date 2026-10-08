package com.project.advanced.project.application.usecase

class CancelarReservaUseCase(
    private val reservaRepository: ReservaRepository,
) {
    fun ejecutar(codigo: CodigoReserva) {
        val reserva = reservaRepository.buscarPorCodigo(codigo)

        if (reserva == null) throw ReservaNoEncontradaException(codigo)

        reserva.cancelar()
        reservaRepository.guardar(reserva)
    }
}
