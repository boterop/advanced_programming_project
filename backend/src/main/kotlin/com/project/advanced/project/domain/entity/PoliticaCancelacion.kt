package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.VersionPolitica

class PoliticaCancelacion private constructor(
    val id: ID,
    val version: VersionPolitica,
    val descripcion: Descripcion,
) {
    companion object {
        fun crear(
            id: ID,
            version: VersionPolitica,
            descripcion: Descripcion,
        ): PoliticaCancelacion = PoliticaCancelacion(id, version, descripcion)
    }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is PoliticaCancelacion && other.id == id
}
