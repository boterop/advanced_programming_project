package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.DiaSemana
import com.project.advanced.project.domain.valueobject.Periodo
import java.time.LocalDate

data class Horario(
    val diaSemana: DiaSemana,
    val horas: List<Periodo>,
) {
    init {
        require(horas.isNotEmpty()) {
            throw ReglaDominioException("El horario debe tener al menos un intervalo de tiempo")
        }
    }

    fun estaDisponible(fecha: LocalDate): Boolean {
        val estaEnHoras = horas.any { it.contiene(fecha) }
        val estaEnDia = diaSemana.esDia(fecha)

        return estaEnHoras && estaEnDia
    }
}
