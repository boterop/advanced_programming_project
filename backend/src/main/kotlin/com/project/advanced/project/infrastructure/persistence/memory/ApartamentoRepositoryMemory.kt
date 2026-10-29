package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.valueobject.ID
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

@Repository
class ApartamentoRepositoryMemory : ApartamentoRepository {
    private val apartamentos: MutableMap<String, Apartamento> = ConcurrentHashMap()

    override fun listar(): List<Apartamento> = apartamentos.values.toList()

    override fun buscar(id: ID): Apartamento? = apartamentos[id.valor]

    override fun guardar(apartamento: Apartamento) {
        apartamentos[apartamento.id.valor] = apartamento
    }
}