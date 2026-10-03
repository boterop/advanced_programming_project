package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Horario
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import java.time.LocalDate

class Alojamiento private constructor(
    val id: ID,
    val nombre: Nombre,
    val apartamentos: List<Apartamento>,
    val politica: PoliticaCancelacion,
    val horarios: List<Horario>,
) {
    companion object {
        fun crear(
            id: ID,
            nombre: Nombre,
            apartamentos: List<Apartamento>,
            politica: PoliticaCancelacion,
            horarios: List<Horario>,
        ): Alojamiento = Alojamiento(id, nombre, apartamentos, politica, horarios)
    }

    fun estaAbierto(fecha: LocalDate): Boolean = horarios.any { it.estaDisponible(fecha) }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Alojamiento && id == other.id
}
