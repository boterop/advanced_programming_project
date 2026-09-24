package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.Tarifa

class Apartamento private constructor(
    val id: IdApartamento,
    val nombre: String,
    val dormitorios: Int,
    val capacidad: Int,
    var estado: EstadoApartamento,
    var activo: Boolean,
    val tarifas: List<Tarifa>,
) {
    init {
        if (id == null) throw ReglaDominioException("El IdApartamento no puede ser nulo")
        if (nombre == null) throw ReglaDominioException("El nombre no puede ser nulo")
        if (dormitorios < 0) throw ReglaDominioException("El dormitorios no puede ser negativo")
        if (capacidad < 0) throw ReglaDominioException("La capacidad no puede ser negativa")
        if (tarifas == null) throw ReglaDominioException("Las tarifas no pueden ser nulas")

        estado = EstadoApartamento.PREPARADO
        activo = true
    }

    companion object {
        fun crear(
            id: IdApartamento,
            nombre: String,
            dormitorios: Int,
            capacidad: Int,
            estado: EstadoApartamento,
            activo: Boolean,
            tarifas: List<Tarifa>,
        ): Apartamento = Apartamento(id, nombre, dormitorios, capacidad, EstadoApartamento.PREPARADO, true, tarifas)
    }

    fun admite(totalOcupantes: Int): Boolean = totalOcupantes <= capacidad

    fun esActivo(): Boolean = activo
}
