package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.ID

/**
 * Suma un ocupante a una reserva modificable (PENDIENTE o CONFIRMADA, sin iniciar). Como cambia la
 * composicion del grupo, revalida la capacidad (RN-02) y recalcula el valor con las tarifas
 * vigentes (RN-14), registrando la diferencia como ajuste en el folio. Valida todo antes de mutar
 * la reserva o el folio, de modo que un rechazo no deja estados a medias.
 */
class RegistroOcupanteService(
    private val cotizarEstanciaService: CotizarEstanciaService
) {
    fun registrar(
        reserva: Reserva,
        folio: Folio,
        ocupante: Ocupante,
        idAjuste: ID,
    ): Dinero {
        reserva.verificarModificable()

        val documento = ocupante.documentoIdentidad.numero
        if (reserva.ocupantes.any { it.documentoIdentidad.numero.equals(documento, ignoreCase = true) }) {
            throw ReglaDominioException("Ya existe un ocupante con ese documento de identidad en la reserva")
        }

        val ocupantes = reserva.ocupantes + ocupante
        val nuevoValor = cotizarEstanciaService.cotizar(reserva.apartamento, reserva.estancia, ocupantes)
        val diferencia = nuevoValor.cantidad() - reserva.valor.cantidad()

        // Los ajustes negativos no son representables con Dinero; se rechaza antes de tocar nada.
        if (diferencia.signum() < 0) {
            throw ReglaDominioException(
                "Las tarifas vigentes reducen el valor de la reserva: el ajuste negativo aun no esta soportado",
            )
        }
        if (diferencia.signum() > 0 && folio.estadoActual() == EstadoFolio.CERRADO) {
            throw ReglaDominioException("El folio esta cerrado: no se puede registrar el ajuste por el nuevo ocupante")
        }

        // Revalida estado, fecha de inicio y capacidad antes de mutar; si falla, nada cambia.
        reserva.modificar(reserva.apartamento, reserva.estancia, ocupantes, reserva.horaLlegada, nuevoValor)

        val ajuste = Dinero(diferencia)
        if (!ajuste.esCero()) folio.registrarCargo(Cargo.crear(idAjuste, ConceptoCargo.AJUSTE, ajuste))

        return ajuste
    }
}