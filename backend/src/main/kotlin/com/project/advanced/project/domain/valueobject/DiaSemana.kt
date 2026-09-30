package com.project.advanced.project.domain.valueobject

import java.time.LocalDate

enum class DiaSemana(
    val iso: Int,
) {
    LUNES(1),
    MARTES(2),
    MIERCOLES(3),
    JUEVES(4),
    VIERNES(5),
    SABADO(6),
    DOMINGO(7),
    ;

    fun esDia(dia: Int): Boolean = dia == iso

    fun esDia(date: LocalDate): Boolean = date.dayOfWeek.getValue() == iso
}
