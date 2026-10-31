package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.EstadoReserva
import java.time.LocalDate

/**
 * Registra los check-in y check-out validando todas las condiciones antes de mutar los agregados,
 * sin dejar estados o procesos a medias en caso de rechazos.
 */
class RegistroEstanciaService {

    /**
     * EstadoReserva CONFIRMADA -> EN_CURSO
     * EstadoApartamento PREPARADO -> OCUPADO
     */
    fun registrarLlegada(
        reserva: Reserva,
        apartamento: Apartamento,
        hoy: LocalDate,
    ) {
        if (reserva.estado != EstadoReserva.CONFIRMADA) {
            throw ReglaDominioException("Solo se puede registrar la llegada de una reserva confirmada")
        }
        if (hoy.isBefore(reserva.estancia.periodo.inicio)) {
            throw ReglaDominioException("La fecha de entrada de la reserva aun no se ha alcanzado")
        }
        if (!apartamento.estado.permiteRegistro()) {
            throw ReglaDominioException("El apartamento no esta preparado para recibir al grupo")
        }

        reserva.estado = EstadoReserva.EN_CURSO
        apartamento.estado = EstadoApartamento.OCUPADO
    }

    /**
     * EstadoReserva EN_CURSO -> FINALIZADA
     * EstadoApartamento OCUPADO -> PENDIENTE_PREPARACION
     * y cierre del folio con saldo en 0
     */
    fun registrarSalida(
        reserva: Reserva,
        apartamento: Apartamento,
        folio: Folio,
    ) {
        if (reserva.estado != EstadoReserva.EN_CURSO) {
            throw ReglaDominioException("Solo se puede registrar la salida de una reserva en curso")
        }
        if (apartamento.estado != EstadoApartamento.OCUPADO) {
            throw ReglaDominioException("El apartamento de la reserva no esta ocupado")
        }
        if (!folio.saldo().esCero()) {
            throw ReglaDominioException("El folio tiene saldo pendiente: no se puede registrar la salida")
        }

        // Si el ultimo pago ya lo cerro, puedeCerrarse() es falso y no se vuelve a cerrar.
        if (folio.puedeCerrarse()) folio.cerrar()

        reserva.estado = EstadoReserva.FINALIZADA
        apartamento.estado = EstadoApartamento.PENDIENTE_PREPARACION
    }
}