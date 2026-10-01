package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.Dormitorio
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Tarifa

class Apartamento private constructor(
    val id: ID,
    val nombre: Nombre,
    val dormitorios: List<Dormitorio>,
    val capacidad: Capacidad,
    var estado: EstadoApartamento,
    var activo: Boolean,
    val tarifas: List<Tarifa>,
) {
    init {
        if (id == null) throw ReglaDominioException("El ID no puede ser nulo")
        if (nombre == null) throw ReglaDominioException("El nombre no puede ser nulo")
        if (capacidad == null) throw ReglaDominioException("La capacidad no puede ser nula")
        if (tarifas == null) throw ReglaDominioException("Las tarifas no pueden ser nulas")
        if (dormitorios.isEmpty()) throw ReglaDominioException("El apartamento debe tener al menos un dormitorio")

        estado = EstadoApartamento.PREPARADO
        activo = true
    }

    companion object {
        fun crear(
            id: ID,
            nombre: Nombre,
            dormitorios: List<Dormitorio>,
            capacidad: Capacidad,
            estado: EstadoApartamento,
            activo: Boolean,
            tarifas: List<Tarifa>,
        ): Apartamento = Apartamento(id, nombre, dormitorios, capacidad, EstadoApartamento.PREPARADO, true, tarifas)
    }

    fun admite(totalOcupantes: Int): Boolean = totalOcupantes <= capacidad.valor

    fun esActivo(): Boolean = activo
}
