package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.time.LocalDate

/**
 * Filtros opcionales para un listado de reservas
 */
data class FiltroReservas(
    val estado: EstadoReserva? = null,
    val fechaDesde: LocalDate? = null,
    val fechaHasta: LocalDate? = null,
    val canalOrigen: CanalOrigen? = null
){
    init {
        if (fechaDesde != null && fechaHasta != null && fechaDesde.isAfter(fechaHasta)) {
            throw ReglaDominioException("La fecha inicial del filtro no puede ser posterior a la fecha final")
        }
    }

    fun admiteEntrada(fechaEntrada: LocalDate): Boolean =
        (fechaDesde == null || !fechaEntrada.isBefore(fechaDesde)) &&
                (fechaHasta == null || !fechaEntrada.isAfter(fechaHasta))
}
