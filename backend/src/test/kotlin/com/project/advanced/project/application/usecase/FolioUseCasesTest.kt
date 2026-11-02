package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.CerrarFolioCommand
import com.project.advanced.project.application.dto.ConsultarFolioQuery
import com.project.advanced.project.application.dto.RegistrarCargoFolioCommand
import com.project.advanced.project.application.dto.RegistrarPagoFolioCommand
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.service.GestionFolioService
import com.project.advanced.project.domain.service.PagarFolioService
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MedioPago
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.fixtures.ReservaFixture.invoke
import com.project.advanced.project.infrastructure.persistence.memory.FolioRepositoryMemory
import com.project.advanced.project.infrastructure.persistence.memory.PagoRepositoryMemory
import com.project.advanced.project.infrastructure.persistence.memory.ReservaRepositoryMemory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class FolioUseCasesTest {
    private val reservas = ReservaRepositoryMemory(1L)
    private val folios = FolioRepositoryMemory()
    private val gestion = GestionFolioService()
    private val pagar = PagarFolioService(PagoRepositoryMemory(), folios)

    private val consultar = ConsultarFolioUseCase(reservas, folios)
    private val registrarCargo = RegistrarCargoFolioUseCase(reservas,
        folios, gestion)
    private val registrarPago = RegistrarPagoFolioUseCase(reservas, folios,
        gestion, pagar)
    private val cerrar = CerrarFolioUseCase(reservas, folios, gestion)

    private val reserva = ReservaFixture()
    private val codigo = reserva.codigo.valor

    init {
        reservas.guardar(reserva)
        val cargoAlojamiento = Cargo.crear(ID(UUID.randomUUID().toString()), ConceptoCargo.ALOJAMIENTO, Dinero(200.0))
        folios.guardar(Folio.crear(reserva.id, mutableListOf(cargoAlojamiento), mutableListOf()))
    }

    private fun monto(valor: Double) = BigDecimal.valueOf(valor)

    private fun assertMonto(
        esperado: Double,
        real: BigDecimal,
    ) = assertEquals(0, monto(esperado).compareTo(real))

    @Test
    fun `should detail charges, payments and the pending balance`() {
        registrarCargo.ejecutar(RegistrarCargoFolioCommand(codigo, ConceptoCargo.SERVICIO, monto(30.0)))
        registrarPago.ejecutar(RegistrarPagoFolioCommand(codigo, MedioPago.EFECTIVO, monto(50.0)))

        val folio = consultar.ejecutar(ConsultarFolioQuery(codigo))

        assertEquals(EstadoFolio.ABIERTO, folio.estado)
        assertEquals(2, folio.cargos.size)
        assertEquals(1, folio.pagos.size)
        assertMonto(230.0, folio.totalCargos)
        assertMonto(50.0, folio.totalPagos)
        assertMonto(180.0, folio.saldoPendiente)
    }

    @Test
    fun `should keep the folio open after a partial payment`() {
        val resultado = registrarPago.ejecutar(RegistrarPagoFolioCommand(
            codigo, MedioPago.PSE, monto(50.0))
        )

        assertFalse(resultado.folioCerrado)
        assertMonto(150.0, resultado.saldoPendiente)
    }

    @Test
    fun `should close the folio when a payment settles the balance`() {
        val resultado = registrarPago.ejecutar(RegistrarPagoFolioCommand(
            codigo, MedioPago.VISA, monto(200.0))
        )

        assertTrue(resultado.folioCerrado)
        assertMonto(0.0, resultado.saldoPendiente)
    }

    @Test
    fun `should reject a payment above the balance and leave the folio untouched`() {
        assertThrows(ReglaDominioException::class.java) {
            registrarPago.ejecutar(RegistrarPagoFolioCommand(codigo,
                MedioPago.VISA, monto(500.0)))
        }

        assertMonto(200.0, consultar.ejecutar(ConsultarFolioQuery(codigo)).saldoPendiente)
    }

    @Test
    fun `should refuse to close a folio with an outstanding balance`() {
        assertThrows(ReglaDominioException::class.java) {
            cerrar.ejecutar(CerrarFolioCommand(codigo))
        }

        assertEquals(EstadoFolio.ABIERTO, consultar.ejecutar(
            ConsultarFolioQuery(codigo)).estado
        )
    }

    @Test
    fun `should fail with a clear error when the reservation does not exist`() {
        assertThrows(ReservaNoEncontradaException::class.java) {
            consultar.ejecutar(ConsultarFolioQuery(UUID.randomUUID().toString()))
        }
    }

    @Test
    fun `should fail with a clear error when the reservation has no folio`() {
        val sinFolio = ReservaFixture()
        reservas.guardar(sinFolio)
        assertThrows(ReglaDominioException::class.java) {
            consultar.ejecutar(ConsultarFolioQuery(sinFolio.codigo.valor))
        }
    }
}