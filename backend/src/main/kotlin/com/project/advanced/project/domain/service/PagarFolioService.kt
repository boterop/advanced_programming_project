package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Pago
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.PagoRepository
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MedioPago
import java.util.UUID

class PagarFolioService(
    val pagoRepository: PagoRepository,
    val folioRepository: FolioRepository,
) {
    fun pagar(
        folioId: ID,
        medioPago: MedioPago,
        valor: Dinero,
    ) {
        var folio = folioRepository.buscar(folioId)

        if (folio == null) {
            throw Exception("No existe el folio con el id $folioId")
        }

        val pago = Pago.crear(ID(UUID.randomUUID().toString()), medioPago, valor)

        folio.registrarPago(pago)

        if (folio.puedeCerrarse()) {
            folio.cerrar()
        }

        folioRepository.guardar(folio)
        pagoRepository.guardar(pago)
    }
}
