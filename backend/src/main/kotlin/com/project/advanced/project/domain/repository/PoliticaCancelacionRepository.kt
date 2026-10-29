package com.project.advanced.project.domain.repository

import com.project.advanced.project.domain.entity.PoliticaCancelacion

interface PoliticaCancelacionRepository {
    val politicas: MutableMap<String, PoliticaCancelacion>

    fun buscarVigente(): PoliticaCancelacion?

    fun guardar(politica: PoliticaCancelacion)
}