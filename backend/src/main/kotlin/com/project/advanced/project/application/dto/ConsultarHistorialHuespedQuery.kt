package com.project.advanced.project.application.dto

data class ConsultarHistorialHuespedQuery(
    val idTitular: String,
    /** Base 1. El tamano de pagina no es configurable: siempre 10. */
    val pagina: Int = 1
)
