package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.valueobject.ID

class FolioRepositoryMemory : FolioRepository {
    val folios: HashMap<String, Folio> = HashMap()

    override fun listar(): List<Folio> = folios.values.toList()

    override fun buscar(id: ID): Folio? = folios[id.valor]

    override fun guardar(folio: Folio) {
        folios.put(folio.id.valor, folio)
    }

    override fun actualizar(folio: Folio) {
        folios.put(folio.id.valor, folio)
    }

    override fun eliminar(folio: Folio) {
        folios.remove(folio.id.valor)
    }
}
