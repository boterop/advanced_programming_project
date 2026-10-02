package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Pago
import com.project.advanced.project.domain.valueobject.ID

interface PagoRepository {
    fun listar(): List<Pago>

    fun buscar(id: ID): Pago?

    fun guardar(pago: Pago)

    fun actualizar(pago: Pago)

    fun eliminar(pago: Pago)
}
