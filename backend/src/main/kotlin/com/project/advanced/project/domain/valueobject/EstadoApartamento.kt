package com.project.advanced.project.domain.valueobject

enum class EstadoApartamento {
    PREPARADO,
    OCUPADO,
    PENDIENTE_PREPARACION,
    EN_PREPARACION,
    FUERA_DE_SERVICIO
    ;

    fun permiteRegistro(): Boolean = this == PREPARADO
}
