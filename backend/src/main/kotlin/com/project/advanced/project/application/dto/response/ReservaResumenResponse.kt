package com.project.advanced.project.application.dto.response

/**
 * Representa la información que se va a mostrar en la vista de detalle de una reserva
 */
data class ReservaResumenResponse(
    val id: ID,
    val codigo: String,
    val apartamento: String,
    val estancia: String,
    val estado: EstadoReserva,
    val canalOrigen: CanalOrigen,
    val idExterno: IdExterno,
    val titular: String,
    val ocupantes: List<String>,
    val horaLlegada: HoraLlegada,
    val valor: String,
    val fechaCreacion: LocalDate,
    val motivoCancelacion: String,
)
