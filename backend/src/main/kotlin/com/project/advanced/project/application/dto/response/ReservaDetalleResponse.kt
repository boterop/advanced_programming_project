package com.project.advanced.project.application.dto.response

data class ReservaDetalleResponse(
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
