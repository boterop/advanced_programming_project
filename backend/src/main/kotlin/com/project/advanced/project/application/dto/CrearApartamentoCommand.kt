package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.Dormitorio
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import java.math.BigDecimal

data class CrearApartamentoCommand(
    val nombre: String,
    val capacidad: Int,
    val dormitorios: List<DormitorioCommand>,
    val tarifas: List<TarifaCommand> /**Extrae tarifa por temporada */
)

data class DormitorioCommand(
    val nombre: String,
    val descripcion: String,
) {
    fun aDominio(): Dormitorio = Dormitorio(Nombre(nombre), Descripcion(descripcion))
}

data class TarifaCommand(
    val temporada: Temporada,
    val valor: BigDecimal, /**Valor por ocupante dacturable, por noche*/
) {
    fun aDominio(): Tarifa = Tarifa(temporada, Dinero(valor))
}