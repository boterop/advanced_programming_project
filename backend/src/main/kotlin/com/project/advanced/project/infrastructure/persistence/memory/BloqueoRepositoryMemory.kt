package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class BloqueoRepositoryMemory : BloqueoRepository {
    private val bloqueos: MutableMap<String, Bloqueo> = ConcurrentHashMap()

    override fun listar(): List<Bloqueo> = bloqueos.values.toList()

    override fun buscar(id: ID): Bloqueo? = bloqueos[id.valor]

    override fun buscarVigentesPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Bloqueo> =
        bloqueos.values
            .filter { it.apartamentoId == apartamentoId }
            .filter { it.vigente }
            .filter { it.periodo.solapa(periodo) }

    override fun guardar(bloqueo: Bloqueo) {
        bloqueos[bloqueo.id.valor] = bloqueo
    }

    override fun eliminar(bloqueo: Bloqueo) {
        bloqueos.remove(bloqueo.id.valor)
    }
}