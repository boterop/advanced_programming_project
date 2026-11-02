package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.Dormitorio
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada

class CatalogoApartamentoService(
    private val apartamentoRepository: ApartamentoRepository
) {
    fun crear(
        id: ID,
        nombre: Nombre,
        dormitorios: List<Dormitorio>,
        capacidad: Capacidad,
        tarifas: List<Tarifa>,
    ): Apartamento {
        if (capacidad.valor < 1) throw ReglaDominioException("La capacidad debe ser de al menos 1 ocupante")

        if (apartamentoRepository.listar().any { it.nombre.valor.equals(nombre.valor, ignoreCase = true) }) {
            throw ReglaDominioException("Ya existe un apartamento con el nombre ${nombre.valor}")
        }

        verificarTarifasCompletas(tarifas)

        // Un apartamento nuevo nace activo y PREPARADO.
        return Apartamento.crear(id, nombre, dormitorios, capacidad, EstadoApartamento.PREPARADO, true, tarifas)
    }

    /**
     * Verifica que todo apartamento activa tenga tarifa > 0 */
    private fun verificarTarifasCompletas(tarifas: List<Tarifa>) {
        val repetidas = tarifas.groupBy { it.temporada }.filterValues { it.size > 1 }.keys
        if (repetidas.isNotEmpty()) {
            throw ReglaDominioException("Hay mas de una tarifa para las temporadas: ${repetidas.joinToString()}")
        }

        val faltantes = Temporada.entries - tarifas.map { it.temporada }.toSet()
        if (faltantes.isNotEmpty()) {
            throw ReglaDominioException("Faltan tarifas para las temporadas: ${faltantes.joinToString()}")
        }

        if (tarifas.any { it.valor.esCero() }) {
            throw ReglaDominioException("Las tarifas deben ser mayores que cero")
        }
    }
}