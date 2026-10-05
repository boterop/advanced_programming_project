package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.FechaCreacion
import com.project.advanced.project.domain.valueobject.Gravedad
import com.project.advanced.project.domain.valueobject.ID

class Novedad private constructor(
    val id: ID,
    val apartamento: Apartamento,
    val descripcion: Descripcion,
    val fechaCreacion: FechaCreacion,
    val autor: Ocupante,
    val gravedad: Gravedad,
) {
    companion object {
        fun crear(
            id: ID,
            apartamento: Apartamento,
            descripcion: Descripcion,
            fechaCreacion: FechaCreacion,
            autor: Ocupante,
            gravedad: Gravedad,
        ): Novedad = Novedad(id, apartamento, descripcion, fechaCreacion, autor, gravedad)
    }
}
