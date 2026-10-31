package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.EstadoReserva
import java.time.LocalDate

/** Todos los filtros son opcionales. El tamano de pagina no es configurable: siempre 10. */
data class ListarReservasQuery(
    val estado: EstadoReserva? = null,
    /** Limite inferior (inclusivo) de la fecha de entrada. */
    val fechaDesde: LocalDate? = null,
    /** Limite superior (inclusivo) de la fecha de entrada. */
    val fechaHasta: LocalDate? = null,
    val canalOrigen: CanalOrigen? = null,
    /** Base 1. */
    val pagina: Int = 1,
)
