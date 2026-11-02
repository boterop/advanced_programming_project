package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Pago
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MedioPago
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.ApartamentoFixture.invoke
import com.project.advanced.project.fixtures.OcupanteFixture
import com.project.advanced.project.fixtures.OcupanteFixture.invoke
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.fixtures.ReservaFixture.invoke
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class RegistroOcupanteServiceTest {
    private val cotizar = CotizarEstanciaService()
    private val servicio = RegistroOcupanteService(cotizar)
    private val titular = OcupanteFixture(documento = "TITULAR1")

    private fun nuevoId() = ID(UUID.randomUUID().toString())

    private fun escenario(
        capacidad: Int = 10,
        estado: EstadoReserva = EstadoReserva.CONFIRMADA,
    ): Pair<Reserva, Folio> {
        val apartamento =
            ApartamentoFixture(
                capacidad = Capacidad(capacidad),
                tarifas = Temporada.entries.map { Tarifa(it, Dinero(100.0)) },
            )
        val reserva = ReservaFixture(apartamento = apartamento, titular = titular, estado = estado, valor = 300.0)
        val alojamiento = Cargo.crear(nuevoId(), ConceptoCargo.ALOJAMIENTO, Dinero(300.0))
        val folio = Folio.crear(reserva.id, mutableListOf(alojamiento), mutableListOf())
        return reserva to folio
    }

    private fun assertMonto(
        esperado: Double,
        real: BigDecimal,
    ) = assertEquals(0, BigDecimal.valueOf(esperado).compareTo(real))

    private val adulto = OcupanteFixture(documento = "ADULTO2",
        fechaNacimiento = LocalDate.of(1985, 5, 5))
    private val menor = OcupanteFixture(documento = "MENOR3",
        fechaNacimiento = LocalDate.now().minusYears(5))

    @Test
    fun `should add a billable occupant, recalculate the value and charge the difference as an adjustment`() {
        val (reserva, folio) = escenario()
        val ajuste = servicio.registrar(reserva, folio, adulto, nuevoId())
        assertEquals(2, reserva.ocupantes.size)
        assertMonto(600.0, reserva.valor.cantidad())
        assertMonto(300.0, ajuste.cantidad())
        assertEquals(ConceptoCargo.AJUSTE, folio.listarCargos().last().concepto)
        assertMonto(600.0, folio.saldo().cantidad())
    }

    @Test
    fun `should add a non billable occupant without changing the value or the folio`() {
        val (reserva, folio) = escenario()
        val ajuste = servicio.registrar(reserva, folio, menor, nuevoId())
        assertEquals(2, reserva.ocupantes.size)
        assertMonto(300.0, reserva.valor.cantidad())
        assertEquals(true, ajuste.esCero())
        assertEquals(1, folio.listarCargos().size)
    }

    @Test
    fun `should reject a document already present in the reservation, ignoring case`() {
        val (reserva, folio) = escenario()
        val repetido = OcupanteFixture(documento = "titular1")
        assertThrows(ReglaDominioException::class.java) {
            servicio.registrar(reserva, folio, repetido, nuevoId())
        }
        assertEquals(1, reserva.ocupantes.size)
    }

    @Test
    fun `should reject exceeding the apartment capacity and leave reservation and folio untouched`() {
        val (reserva, folio) = escenario(capacidad = 1)
        assertThrows(ReglaDominioException::class.java) {
            servicio.registrar(reserva, folio, adulto, nuevoId())
        }
        assertEquals(1, reserva.ocupantes.size)
        assertMonto(300.0, reserva.valor.cantidad())
        assertEquals(1, folio.listarCargos().size)
    }

    @Test
    fun `should reject a reservation that is not modifiable`() {
        listOf(EstadoReserva.EN_CURSO, EstadoReserva.CANCELADA, EstadoReserva.FINALIZADA).forEach { estado ->
            val (reserva, folio) = escenario(estado = estado)
            assertThrows(ReglaDominioException::class.java) {
                servicio.registrar(reserva, folio, adulto, nuevoId())
            }
        }
    }

    @Test
    fun `should reject a billable occupant when the folio is closed and leave the reservation untouched`() {
        val (reserva, folio) = escenario()
        folio.registrarPago(Pago.crear(nuevoId(), MedioPago.EFECTIVO, Dinero(300.0)))
        folio.cerrar()
        assertThrows(ReglaDominioException::class.java) {
            servicio.registrar(reserva, folio, adulto, nuevoId())
        }
        assertEquals(1, reserva.ocupantes.size)
        assertMonto(300.0, reserva.valor.cantidad())
        assertEquals(EstadoFolio.CERRADO, folio.estadoActual())
    }

    @Test
    fun `should still add a non billable occupant when the folio is closed`() {
        val (reserva, folio) = escenario()
        folio.registrarPago(Pago.crear(nuevoId(), MedioPago.EFECTIVO, Dinero(300.0)))
        folio.cerrar()
        servicio.registrar(reserva, folio, menor, nuevoId())
        assertEquals(2, reserva.ocupantes.size)
    }
}