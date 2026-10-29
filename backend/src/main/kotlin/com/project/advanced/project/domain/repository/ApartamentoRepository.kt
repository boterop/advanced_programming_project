package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.valueobject.ID

interface ApartamentoRepository {
    val apartamentos: MutableMap<String, Apartamento>

    fun listar(): List<Apartamento>

    fun buscar(id: ID): Apartamento?

    fun guardar(apartamento: Apartamento)
}