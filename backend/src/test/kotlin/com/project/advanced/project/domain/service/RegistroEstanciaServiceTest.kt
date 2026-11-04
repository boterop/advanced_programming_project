package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Pago
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MedioPago
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.ApartamentoFixture.invoke
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.fixtures.ReservaFixture.invoke
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class RegistroEstanciaServiceTest {
    private val servicio = RegistroEstanciaService()

    private fun folio(
        cargo: Double,
        pagado: Double,
    ): Folio {
        val folio =
            Folio.crear(
                ID(UUID.randomUUID().toString()),
                mutableListOf(Cargo.crear(ID(UUID.randomUUID().toString()), ConceptoCargo.ALOJAMIENTO, Dinero(cargo))),
                mutableListOf(),
            )
        if (pagado > 0) folio.registrarPago(Pago.crear(ID(UUID.randomUUID().toString()), MedioPago.EFECTIVO, Dinero(pagado)))
        return folio
    }

    @Nested
    inner class RegistrarLlegada {
        @Test
        fun `should move the reservation to EN_CURSO and the apartment to OCUPADO`() {
            val apartamento = ApartamentoFixture()
            val reserva = ReservaFixture(apartamento = apartamento, estado = EstadoReserva.CONFIRMADA)

            servicio.registrarLlegada(reserva, apartamento, LocalDate.now())

            assertEquals(EstadoReserva.EN_CURSO, reserva.estado)
            assertEquals(EstadoApartamento.OCUPADO, apartamento.estado)
        }

        @Test
        fun `should reject a reservation that is not confirmed`() {
            val apartamento = ApartamentoFixture()
            val reserva = ReservaFixture(apartamento = apartamento, estado = EstadoReserva.PENDIENTE)

            assertThrows(ReglaDominioException::class.java) {
                servicio.registrarLlegada(reserva, apartamento, LocalDate.now())
            }
            assertEquals(EstadoApartamento.PREPARADO, apartamento.estado)
        }

        @Test
        fun `should reject check-in before the entry date`() {
            val apartamento = ApartamentoFixture()
            val reserva = ReservaFixture(apartamento = apartamento, estado = EstadoReserva.CONFIRMADA)

            assertThrows(ReglaDominioException::class.java) {
                servicio.registrarLlegada(reserva, apartamento, LocalDate.now().minusDays(1))
            }
            assertEquals(EstadoReserva.CONFIRMADA, reserva.estado)
        }

        @Test
        fun `should reject an apartment that is not prepared and leave the reservation untouched`() {
            val apartamento = ApartamentoFixture()
            apartamento.estado = EstadoApartamento.PENDIENTE_PREPARACION
            val reserva = ReservaFixture(apartamento = apartamento, estado = EstadoReserva.CONFIRMADA)

            assertThrows(ReglaDominioException::class.java) {
                servicio.registrarLlegada(reserva, apartamento, LocalDate.now())
            }
            assertEquals(EstadoReserva.CONFIRMADA, reserva.estado)
        }
    }

    @Nested
    inner class RegistrarSalida {
        private fun enCurso(): Pair<Reserva, Apartamento> {
            val apartamento = ApartamentoFixture()
            apartamento.estado = EstadoApartamento.OCUPADO
            return ReservaFixture(apartamento = apartamento, estado = EstadoReserva.EN_CURSO) to apartamento
        }

        @Test
        fun `should finish the reservation, free the apartment for cleaning and close a settled folio`() {
            val (reserva, apartamento) = enCurso()
            val folio = folio(cargo = 200.0, pagado = 200.0)

            servicio.registrarSalida(reserva, apartamento, folio)

            assertEquals(EstadoReserva.FINALIZADA, reserva.estado)
            assertEquals(EstadoApartamento.PENDIENTE_PREPARACION, apartamento.estado)
            assertTrue(folio.saldo().esCero())
        }

        @Test
        fun `should reject check-out with an outstanding balance and change nothing`() {
            val (reserva, apartamento) = enCurso()
            val folio = folio(cargo = 200.0, pagado = 50.0)

            assertThrows(ReglaDominioException::class.java) { servicio.registrarSalida(reserva, apartamento, folio) }

            assertEquals(EstadoReserva.EN_CURSO, reserva.estado)
            assertEquals(EstadoApartamento.OCUPADO, apartamento.estado)
        }

        @Test
        fun `should reject check-out of a reservation that is not in progress`() {
            val apartamento = ApartamentoFixture()
            apartamento.estado = EstadoApartamento.OCUPADO
            val reserva = ReservaFixture(apartamento = apartamento, estado = EstadoReserva.CONFIRMADA)

            assertThrows(ReglaDominioException::class.java) {
                servicio.registrarSalida(reserva, apartamento, folio(cargo = 200.0, pagado = 200.0))
            }
        }
    }
}