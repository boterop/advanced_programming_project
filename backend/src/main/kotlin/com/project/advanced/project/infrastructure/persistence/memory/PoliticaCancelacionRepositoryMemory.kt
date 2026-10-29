package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.PoliticaCancelacion
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class PoliticaCancelacionRepositoryMemory {
    private val politicas: MutableMap<String, PoliticaCancelacion> = ConcurrentHashMap()

    fun buscarVigente(): PoliticaCancelacion? = politicas.values.maxByOrNull { it.version.version }

    fun guardar(politica: PoliticaCancelacion) {
        politicas[politica.id.valor] = politica
    }
}