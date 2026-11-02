package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import kotlin.collections.orEmpty

/**
 * Se encarga del cambio de estado operativo del apartamento
 * PENDIENTE_PREPARACION -> EN_PREPARACION -> PREPARADO
 */
class EstadoOperativoApartamentoService {
    fun cambiar(
        apartamento: Apartamento,
        nuevoEstado: EstadoApartamento,
    ) {
        if (nuevoEstado == EstadoApartamento.FUERA_DE_SERVICIO) {
            throw ReglaDominioException("Declarar un apartamento fuera de servicio es una decision administrativa")
        }
        if (nuevoEstado !in TRANSICIONES_DE_SERVICIO[apartamento.estado].orEmpty()) {
            throw ReglaDominioException(
                "Transicion de estado operativo no permitida: ${apartamento.estado} -> $nuevoEstado",
            )
        }

        apartamento.estado = nuevoEstado
    }

    private companion object {
        val TRANSICIONES_DE_SERVICIO: Map<EstadoApartamento, Set<EstadoApartamento>> =
            mapOf(
                EstadoApartamento.PENDIENTE_PREPARACION to setOf(EstadoApartamento.EN_PREPARACION),
                EstadoApartamento.EN_PREPARACION to setOf(EstadoApartamento.PREPARADO),
            )
    }
}