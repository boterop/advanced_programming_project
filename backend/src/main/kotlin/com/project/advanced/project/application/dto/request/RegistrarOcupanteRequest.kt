package com.project.advanced.project.application.dto.request

data class RegistrarOcupanteRequest(
    val codigoReserva: String,
    val ocupante: OcupanteRequest
)