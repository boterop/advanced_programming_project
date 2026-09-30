package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.VersionPolitica

class PoliticaCancelacion private constructor(
    val version: VersionPolitica,
    val descripcion: Descripcion,
) {
    companion object {
        fun crear(
            version: VersionPolitica,
            descripcion: Descripcion,
        ): PoliticaCancelacion = PoliticaCancelacion(version, descripcion)
    }

    override fun hashCode(): Int = version.hashCode()

    override fun equals(other: Any?): Boolean = other is PoliticaCancelacion && other.version == version
}
