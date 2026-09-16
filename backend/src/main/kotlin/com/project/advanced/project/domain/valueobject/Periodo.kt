package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Periodo(
    val inicio: LocalDate,
    val fin: LocalDate,
) {
    init {
        if (inicio.isAfter(fin)) {
            throw ReglaDominioException("El inicio del periodo no puede ser posterior al fin")
        }
        if (inicio == null || fin == null) {
            throw ReglaDominioException("El periodo no puede ser nulo")
        }
    }

    fun noches(): Int = inicio.until(fin, ChronoUnit.DAYS)

    fun contiene(fecha: LocalDate): Boolean = fecha.isAfter(this.inicio) && fecha.isBefore(this.fin)

    fun solapa(
        inicio: LocalDate,
        fin: LocalDate,
    ): Boolean {
        if (inicio.isAfter(this.inicio) && inicio.isBefore(this.fin)) {
            return true
        }

        if (fin.isAfter(this.inicio) && fin.isBefore(this.fin)) {
            return true
        }

        if (this.inicio.isAfter(inicio) && this.inicio.isBefore(fin)) {
            return true
        }

        if (this.fin.isAfter(inicio) && this.fin.isBefore(fin)) {
            return true
        }

        return false
    }

    fun solapa(periodo: Periodo): Boolean = this.solapa(periodo.inicio, periodo.fin)
}
