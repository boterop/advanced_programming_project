package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException

data class Pagina<T>(
    val contenido: List<T>,
    val numero: Int,
    val tamano: Int,
    val totalElementos: Long
){
    init {
        if (numero < 1) throw ReglaDominioException("El numero de pagina debe ser mayor o igual a 1")
        if (tamano < 1) throw ReglaDominioException("El tamano de pagina debe ser mayor o igual a 1")
    }

    val totalPaginas: Int
        get() = ((totalElementos + tamano - 1) / tamano).toInt()

    companion object {
        /** Paginacion de 10 resultados por pagina en todos los listados. */
        const val TAMANO = 10
    }
}
