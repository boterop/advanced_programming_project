package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.FechaCreacion
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MedioPago
import java.time.LocalDate

class Pago private constructor(
    val id: ID,
    val valor: Dinero,
    val medioPago: MedioPago,
    var fechaCreacion: FechaCreacion,
) {
    companion object {
        fun crear(
            id: ID,
            medioPago: MedioPago,
            valor: Dinero,
        ): Pago = Pago(id, valor, medioPago, FechaCreacion(LocalDate.now()))
    }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Pago && id == other.id
}
