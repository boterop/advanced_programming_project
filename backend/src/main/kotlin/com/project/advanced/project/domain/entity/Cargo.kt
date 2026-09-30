package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero

class Cargo private constructor(
    val concepto: ConceptoCargo,
    val valor: Dinero,
) {
    companion object {
        fun crear(
            concepto: ConceptoCargo,
            valor: Dinero,
        ): Cargo = Cargo(concepto, valor)
    }
}
