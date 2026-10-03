package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.ID

class Cargo private constructor(
    val id: ID,
    val concepto: ConceptoCargo,
    val valor: Dinero,
) {
    companion object {
        fun crear(
            id: ID,
            concepto: ConceptoCargo,
            valor: Dinero,
        ): Cargo = Cargo(id, concepto, valor)
    }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Cargo && id == other.id
}
