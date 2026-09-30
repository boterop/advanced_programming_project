package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Horario
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.VersionPolitica
import java.time.LocalDate

class Alojamiento private constructor(
    val nombre: Nombre,
    val apartamentos: List<Apartamento>,
    val politica: VersionPolitica,
    val horarios: List<Horario>,
) {
    fun estaAbierto(fecha: LocalDate): Boolean = horarios.any { it.estaDisponible(fecha) }
}
