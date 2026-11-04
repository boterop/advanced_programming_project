package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.FiltroReservas
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Pagina
import com.project.advanced.project.domain.valueobject.Periodo

interface ReservaRepository {
    fun listar(): List<Reserva>

    fun buscar(id: ID): Reserva?

    fun buscar(
        filtro: FiltroReservas,
        numeroPagina: Int,
        tamano: Int,
    ): Pagina<Reserva>

    fun buscarPorCodigo(codigo: CodigoReserva): Reserva?

    fun buscarActivasPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Reserva>

    fun guardar(reserva: Reserva)

    fun eliminar(reserva: Reserva)

    /** Dias que una reserva PENDIENTE puede esperar su confirmacion (RN-21). */
    fun plazoConfirmacionDias(): Long
}
