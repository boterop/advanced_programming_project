package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.valueobject.ID

interface FolioRepository {
    fun listar(): List<Folio>

    fun buscar(id: ID): Folio?

    fun guardar(folio: Folio)

    fun actualizar(folio: Folio)

    fun eliminar(folio: Folio)
}
