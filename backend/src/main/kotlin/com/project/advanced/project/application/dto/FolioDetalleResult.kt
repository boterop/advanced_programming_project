package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.MedioPago
import java.math.BigDecimal
import java.time.LocalDate

data class FolioDetalleResult(
    val codigoReserva: String,
    val estado: EstadoFolio,
    val totalCargos: BigDecimal,
    val totalPagos: BigDecimal,
    val saldoPendiente: BigDecimal,
    val cargos: List<CargoResult>,
    val pagos: List<PagoResult>
)

data class CargoResult(
    val id: String,
    val concepto: ConceptoCargo,
    val valor: BigDecimal
)

data class PagoResult(
    val id: String,
    val medioPago: MedioPago,
    val valor: BigDecimal,
    val fecha: LocalDate
)

fun Folio.aDetalle(codigoReserva: String): FolioDetalleResult {
    val cargos = listarCargos()
    val pagos = listarPagos()

    return FolioDetalleResult(
        codigoReserva = codigoReserva,
        estado = estadoActual(),
        totalCargos = cargos.fold(BigDecimal.ZERO) { total, cargo -> total + cargo.valor.cantidad() },
        totalPagos = pagos.fold(BigDecimal.ZERO) { total, pago -> total + pago.valor.cantidad() },
        saldoPendiente = saldo().cantidad(),
        cargos = cargos.map { CargoResult(it.id.valor, it.concepto, it.valor.cantidad()) },
        pagos = pagos.map { PagoResult(it.id.valor, it.medioPago, it.valor.cantidad(), it.fechaCreacion.valor) }
    )
}
