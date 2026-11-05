package com.project.advanced.project.application.dto.request

import com.project.advanced.project.domain.valueobject.EstadoApartamento

data class CambiarEstadoApartamentoRequest(
    val apartamentoId: String,
    val nuevoEstado: EstadoApartamento
)