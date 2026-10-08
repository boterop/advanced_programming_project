package com.project.advanced.project.application.dto.request

data class CrearReservaRequest(
    @field:NotBlank(message = "El id del apartamento no puede estar vacio")
    val idApartamento: String,
    @field:NotNull(message = "La fecha de entrada es obligatoria")
    val fechaEntrada: LocalDate,
    @field:NotNull(message = "La hora de salida es obligatoria")
    val fechaSalida: LocalDate,
    @field:NotNull(message = "El canal de origen es obligatorio")
    val canalOrigen: CanalOrigen,
    @field:NotNull(message = "El titular es obligatorio")
    @field:Valid
    val titular: OcupanteRequest,
    @field:NotNull(message = "La reserva debe tener al menos 1 ocupante")
    @field:Valid
    val ocupantes: List<OcupanteRequest>,
)
