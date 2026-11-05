package com.project.advanced.project.application.dto.response

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.valueobject.Temporada
import java.math.BigDecimal

data class ApartamentoDisponibleResult(
    val id: String,
    val nombre: String,
    val capacidad: Int,
    val dormitorios: List<DormitorioResult>,
    val tarifas: List<TarifaResult>
)

data class DormitorioResult(
    val nombre: String,
    val descripcion: String,
)

data class TarifaResult(
    val temporada: Temporada,
    val valor: BigDecimal, /** Valor por ocupante facturable, por noche. */
)

fun Apartamento.aDisponible(): ApartamentoDisponibleResult =
    ApartamentoDisponibleResult(
        id = id.valor,
        nombre = nombre.valor,
        capacidad = capacidad.valor,
        dormitorios = dormitorios.map { DormitorioResult(it.nombre.valor, it.descripcion.descripcion) },
        tarifas = tarifas.map { TarifaResult(it.temporada, it.valor.cantidad()) },
    )
