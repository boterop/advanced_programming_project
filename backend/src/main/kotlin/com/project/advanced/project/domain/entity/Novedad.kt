package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.FechaCreacion
import com.project.advanced.project.domain.valueobject.Gravedad

class Novedad private constructor(
    val apartamento: Apartamento,
    val descripcion: Descripcion,
    val fechaCreacion: FechaCreacion,
    val autor: Ocupante,
    val gravedad: Gravedad,
) {
    companion object {
        fun crear(
            apartamento: Apartamento,
            descripcion: Descripcion,
            fechaCreacion: FechaCreacion,
            autor: Ocupante,
            gravedad: Gravedad,
        ): Novedad = Novedad(apartamento, descripcion, fechaCreacion, autor, gravedad)
    }
}
