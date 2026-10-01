package com.project.advanced.project.fixtures

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.Dormitorio
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Tarifa
import java.util.UUID

object ApartamentoFixture {
    operator fun invoke(
        nombre: Nombre = Nombre("Apartamento 1"),
        dormitorios: List<Dormitorio> = listOf(Dormitorio(Nombre("Dormitorio 1"), Descripcion("Dormitorio central"))),
        capacidad: Capacidad = Capacidad(10),
        estado: EstadoApartamento = EstadoApartamento.PREPARADO,
        activo: Boolean = true,
        tarifas: List<Tarifa> = emptyList(),
    ) = Apartamento.crear(
        ID(UUID.randomUUID().toString()),
        nombre,
        dormitorios,
        capacidad,
        estado,
        activo,
        tarifas,
    )
}
