package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.TiempoPreparacion
import java.time.LocalDate

class BusquedaDisponibilidadService(
    private val apartamentoRepository: ApartamentoRepository,
    private val disponibilidadApartamentoService: DisponibilidadApartamentoService
) {
    fun buscarDisponibles(
        estancia: Estancia,
        cantidadOcupantes: Int,
        tiempoPreparacion: TiempoPreparacion,
        hoy: LocalDate = LocalDate.now()
    ): List<Apartamento> {
        if (cantidadOcupantes < 1) throw ReglaDominioException("El grupo debe tener minimo 1 ocupante")
        if (estancia.periodo.inicio.isBefore(hoy)) {
            throw ReglaDominioException("La fecha de entrada no puede ser anterior a la fecha actual")
        }

        return apartamentoRepository
            .listar()
            .filter { it.esActivo() }
            .filter { it.admite(cantidadOcupantes) }
            .filter { estaLibre(it, estancia, tiempoPreparacion) }
            .sortedBy { it.nombre.valor }
    }

    private fun estaLibre(
        apartamento: Apartamento,
        estancia: Estancia,
        tiempoPreparacion: TiempoPreparacion,
    ): Boolean =
        try {
            disponibilidadApartamentoService.verificarDisponibilidad(apartamento.id, estancia, tiempoPreparacion)
            true
        } catch (e: ReglaDominioException) {
            false
        }
}