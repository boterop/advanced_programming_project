package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Bloqueo
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class BloqueoRepositoryMemory {
    private val bloqueos: MutableMap<String, Bloqueo> = ConcurrentHashMap()

    fun listar(): List<Bloqueo> = bloqueos.values.toList()

    fun buscar(id: ID): Bloqueo? = bloqueos[id.valor]

    fun buscarVigentesPorApartamento(
        apartamentoId: ID,
        periodo: Periodo,
    ): List<Bloqueo> =
        bloqueos.values
            .filter { it.apartamentoId == apartamentoId }
            .filter { it.vigente }
            .filter { it.periodo.solapa(periodo) }

    fun guardar(bloqueo: Bloqueo) {
        bloqueos[bloqueo.id.valor] = bloqueo
    }

    fun eliminar(bloqueo: Bloqueo) {
        bloqueos.remove(bloqueo.id.valor)
    }
}