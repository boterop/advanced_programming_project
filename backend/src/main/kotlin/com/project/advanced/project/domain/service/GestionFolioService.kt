package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.ID

/**
 * Un folio cerrado no admite movimientos y solo se cierra con saldo en cero (RN-17).
 * Los pagos los aplica [PagarFolioService]; este servicio solo verifica sus precondiciones.
 */
class GestionFolioService {
    /**
     * Registra un cargo manual.
     */
    fun registrarCargo(
        folio: Folio,
        id: ID,
        concepto: ConceptoCargo,
        valor: Dinero,
    ): Cargo {
        verificarAbierto(folio)
        if (concepto !in CONCEPTOS_MANUALES) {
            throw ReglaDominioException("Solo se pueden registrar manualmente cargos de servicio o penalidad")
        }
        if (valor.esCero()) throw ReglaDominioException("El valor del cargo debe ser mayor que cero")

        val cargo = Cargo.crear(id, concepto, valor)
        folio.registrarCargo(cargo)
        return cargo
    }

    fun verificarPagable(
        folio: Folio,
        valor: Dinero,
    ) {
        verificarAbierto(folio)
        if (valor.esCero()) throw ReglaDominioException("El valor del pago debe ser mayor que cero")
    }

    fun cerrar(folio: Folio) {
        verificarAbierto(folio)
        if (!folio.saldo().esCero()) {
            throw ReglaDominioException("El folio tiene saldo pendiente: no puede cerrarse")
        }
        folio.cerrar()
    }

    private fun verificarAbierto(folio: Folio) {
        if (folio.estadoActual() == EstadoFolio.CERRADO) throw ReglaDominioException("El folio esta cerrado")
    }

    private companion object {
        val CONCEPTOS_MANUALES = setOf(ConceptoCargo.SERVICIO, ConceptoCargo.PENALIDAD)
    }
}