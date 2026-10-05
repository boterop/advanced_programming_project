package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.ID

class Folio private constructor(
    val id: ID,
    private val cargos: MutableList<Cargo>,
    private val pagos: MutableList<Pago>,
    private var estado: EstadoFolio,
) {
    companion object {
        fun crear(
            id: ID,
            cargos: MutableList<Cargo>,
            pagos: MutableList<Pago>,
        ): Folio = Folio(id, cargos, pagos, EstadoFolio.ABIERTO)
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

    fun cerrar() {
        estado = EstadoFolio.CERRADO
    }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Folio && id == other.id
}
