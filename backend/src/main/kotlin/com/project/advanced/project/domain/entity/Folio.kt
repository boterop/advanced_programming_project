package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio

class Folio private constructor(
    private val cargos: MutableList<Cargo>,
    private val pagos: MutableList<Pago>,
    val estado: EstadoFolio,
) {
    companion object {
        fun crear(
            cargos: MutableList<Cargo>,
            pagos: MutableList<Pago>,
            estado: EstadoFolio,
        ): Folio = Folio(cargos, pagos, estado)
    }

    fun registrarPago(pago: Pago) {
        if (pago.valor.cantidad() > saldo().cantidad()) {
            throw ReglaDominioException("El pago no puede superar el saldo")
        }
        pagos.add(pago)
    }

    fun registrarCargo(cargo: Cargo) = cargos.add(cargo)

    fun saldo(): Dinero {
        val valorCargos = cargos.fold(Dinero(0)) { acum, cargo -> acum.sumar(cargo.valor) }
        val valorPagos = pagos.fold(Dinero(0)) { acum, pago -> acum.sumar(pago.valor) }

        return valorCargos.restar(valorPagos)
    }

    fun puedeCerrarse(): Boolean = estado == EstadoFolio.ABIERTO && saldo().esCero()
}
