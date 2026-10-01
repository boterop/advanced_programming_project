package com.project.advanced.project.domain.valueobject

data class Nombre(
    val valor: String,
) {
    init {
        require(valor.isNotBlank()) { "El nombre no puede ser vacío" }
    }

    override fun hashCode(): Int = valor.hashCode()

    override fun equals(other: Any?): Boolean = other is Nombre && valor == other.valor
}
