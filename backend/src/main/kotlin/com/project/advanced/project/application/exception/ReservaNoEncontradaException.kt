package com.project.advanced.project.application.exception

import com.project.advanced.project.domain.valueobject.CodigoReserva

class ReservaNoEncontradaException(
    codigo: CodigoReserva,
) : Exception("No se encontro la reserva con codigo $codigo.valor")
