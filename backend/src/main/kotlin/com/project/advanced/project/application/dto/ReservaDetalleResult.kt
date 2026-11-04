package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.EstadoReserva
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class ReservaDetalleResult(
    val id: String,
    val codigo: String,
    val estado: EstadoReserva,
    val canalOrigen: CanalOrigen,
    val idExterno: String,
    val apartamentoId: String,
    val apartamentoNombre: String,
    val fechaEntrada: LocalDate,
    val fechaSalida: LocalDate,
    val noches: Long,
    val horaLlegada: LocalTime,
    val titular: OcupanteResult,
    val ocupantes: List<OcupanteResult>,
    val valor: BigDecimal,
    val saldo: BigDecimal?, /** Saldo actual del folio; null si la reserva no tiene folio. */
    val politicaVersion: Int,
    val politicaDescripcion: String,
    val fechaCreacion: LocalDate,
    val motivoCancelacion: String
)

data class OcupanteResult(
    val id: String,
    val documentoIdentidad: String,
    val nombre: String,
    val correoElectronico: String,
    val fechaNacimiento: LocalDate,
)

fun Ocupante.aResult(): OcupanteResult =
    OcupanteResult(
        id = id.valor,
        documentoIdentidad = documentoIdentidad.numero,
        nombre = nombre.valor,
        correoElectronico = correoElectronico.valor,
        fechaNacimiento = fechaNacimiento.valor,
    )

fun Reserva.aDetalle(saldo: BigDecimal?): ReservaDetalleResult =
    ReservaDetalleResult(
        id = id.valor,
        codigo = codigo.valor,
        estado = estado,
        canalOrigen = canalOrigen,
        idExterno = idExterno.valor,
        apartamentoId = apartamento.id.valor,
        apartamentoNombre = apartamento.nombre.valor,
        fechaEntrada = estancia.periodo.inicio,
        fechaSalida = estancia.periodo.fin,
        noches = estancia.periodo.noches(),
        horaLlegada = horaLlegada.hora,
        titular = titular.aResult(),
        ocupantes = ocupantes.map { it.aResult() },
        valor = valor.cantidad(),
        saldo = saldo,
        politicaVersion = politica.version.version,
        politicaDescripcion = politica.descripcion.descripcion,
        fechaCreacion = fechaCreacion.valor,
        motivoCancelacion = motivoCancelacion,
    )
