package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo

class ReservaRepositoryMemory : ReservaRepository {
    private val reservas: HashMap<String, Reserva> = HashMap()

    override fun listar(): List<Reserva> = reservas.values.toList()

    override fun buscar(id: ID): Reserva? = reservas[id.valor]

    override fun buscarPorCodigo(codigo: CodigoReserva): Reserva? = reservas.values.find { it.codigo.valor == codigo.valor }

    override fun buscarActivasPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Reserva> =
        reservas.values
            .filter { it.apartamento.id == apartamentoId }
            .filter { it.estado.esActiva() }
            .filter { it.estancia.periodo.solapa(periodo) }
            .toList()

    override fun guardar(reserva: Reserva) {
        reservas.put(reserva.id.valor, reserva)
    }

    override fun eliminar(reserva: Reserva) {
        reservas.remove(reserva.id.valor)
    }
}
