package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.EstadoApartamento

data class CambiarEstadoApartamentoCommand(
    val apartamentoId: String,
    val nuevoEstado: EstadoApartamento
)
