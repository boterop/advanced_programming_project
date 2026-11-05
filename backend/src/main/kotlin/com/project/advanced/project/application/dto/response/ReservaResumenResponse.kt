package com.project.advanced.project.application.dto.response

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.EstadoReserva
import java.math.BigDecimal
import java.time.LocalDate

data class ReservaResumenResult(
    val codigo: String,
    val estado: EstadoReserva,
    val canalOrigen: CanalOrigen,
    val apartamentoNombre: String,
    val titularNombre: String,
    val fechaEntrada: LocalDate,
    val fechaSalida: LocalDate,
    val valor: BigDecimal,
    val fechaCreacion: LocalDate
)
fun Reserva.aResumen(): ReservaResumenResult =
    ReservaResumenResult(
        codigo = codigo.valor,
        estado = estado,
        canalOrigen = canalOrigen,
        apartamentoNombre = apartamento.nombre.valor,
        titularNombre = titular.nombre.valor,
        fechaEntrada = estancia.periodo.inicio,
        fechaSalida = estancia.periodo.fin,
        valor = valor.cantidad(),
        fechaCreacion = fechaCreacion.valor
    )

