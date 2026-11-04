package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.FiltroReservas
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Pagina
import com.project.advanced.project.domain.valueobject.Periodo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class ReservaRepositoryMemory(
    @Value("\${sga.reserva.plazo-confirmacion-dias:1}") private val plazo: Long,
) : ReservaRepository {
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

    override fun buscar(
        filtro: FiltroReservas,
        numeroPagina: Int,
        tamano: Int,
    ): Pagina<Reserva> {
        val coincidentes =
                listar()
                .filter { filtro.estado == null || it.estado == filtro.estado }
                .filter { filtro.canalOrigen == null || it.canalOrigen == filtro.canalOrigen }
                .filter { filtro.admiteEntrada(it.estancia.periodo.inicio) }
                .sortedWith(
                    compareByDescending<Reserva> { it.fechaCreacion.valor }
                        .thenByDescending { it.estancia.periodo.inicio },
                )

        val contenido = coincidentes.drop((numeroPagina - 1) * tamano).take(tamano)

        return Pagina(contenido, numeroPagina, tamano, coincidentes.size.toLong())
    }

    override fun plazoConfirmacionDias(): Long = plazo
}
