package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Pago
import com.project.advanced.project.domain.repository.PagoRepository
import com.project.advanced.project.domain.valueobject.ID

class PagoRepositoryMemory : PagoRepository {
    private val pagos: HashMap<String, Pago> = HashMap()

    override fun listar(): List<Pago> = pagos.values.toList()

    override fun buscar(id: ID): Pago? = pagos[id.valor]

    override fun guardar(pago: Pago) {
        pagos.put(pago.id.valor, pago)
    }

    override fun actualizar(pago: Pago) {
        pagos.put(pago.id.valor, pago)
    }

    override fun eliminar(pago: Pago) {
        pagos.remove(pago.id.valor)
    }
}
