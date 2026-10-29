package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.PoliticaCancelacion
import com.project.advanced.project.domain.repository.PoliticaCancelacionRepository
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class PoliticaCancelacionRepositoryMemory : PoliticaCancelacionRepository {
    private val politicas: MutableMap<String, PoliticaCancelacion> = ConcurrentHashMap()

    override fun buscarVigente(): PoliticaCancelacion? = politicas.values.maxByOrNull { it.version.version }

    override fun guardar(politica: PoliticaCancelacion) {
        politicas[politica.id.valor] = politica
    }
}