package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.entity.PoliticaCancelacion
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.IdExterno
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.OcupanteFixture
import com.project.advanced.project.fixtures.PoliticaCancelacionFixture
import com.project.advanced.project.fixtures.ReservaFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class ReservaTest {
    private val politica = PoliticaCancelacionFixture()
    private val fechaInicio = LocalDate.now().plusDays(10)
    private val titular = OcupanteFixture()

    @Nested
    inner class Crear {
        @Test
        fun `should create a reserva with its supplied values`() {
            val id = UUID.randomUUID().toString()
            val apto = ApartamentoFixture()
            val periodo = Periodo(fechaInicio, fechaInicio.plusDays(2))
            val idExterno = IdExterno(CanalOrigen.DIRECTO, "ext-1")
            val horaLlegada = HoraLlegada(LocalTime.of(15, 0))
            val valor = 200.0

            val reserva =
                Reserva.crear(
                    ID(id),
                    CodigoReserva(id),
                    apto,
                    Estancia(periodo),
                    EstadoReserva.CONFIRMADA,
                    CanalOrigen.DIRECTO,
                    idExterno,
                    titular,
                    listOf(titular),
                    horaLlegada,
                    Dinero(valor),
                    politica,
                    "",
                )

            assertEquals(ID(id), reserva.id)
            assertEquals(CodigoReserva(id), reserva.codigo)
            assertEquals(apto, reserva.apartamento)
            assertEquals(Estancia(periodo), reserva.estancia)
            assertEquals(EstadoReserva.CONFIRMADA, reserva.estado)
            assertEquals(CanalOrigen.DIRECTO, reserva.canalOrigen)
            assertEquals(idExterno, reserva.idExterno)
            assertEquals(titular, reserva.titular)
            assertEquals(listOf(titular), reserva.ocupantes)
            assertEquals(horaLlegada, reserva.horaLlegada)
            assertEquals(Dinero(valor), reserva.valor)
            assertEquals(politica, reserva.politica)
            assertEquals(LocalDate.now(), reserva.fechaCreacion.valor)
            assertEquals("", reserva.motivoCancelacion)
        }
    }

    @Nested
    inner class Cancelar {
        @Test
        fun `should cancel a pending reservation and record the trimmed reason`() {
            val reserva = ReservaFixture(estado = EstadoReserva.PENDIENTE)

            reserva.cancelar("  Cambio de planes  ")

            assertEquals(EstadoReserva.CANCELADA, reserva.estado)
            assertEquals("Cambio de planes", reserva.motivoCancelacion)
        }

        @Test
        fun `should cancel a confirmed reservation`() {
            val reserva = ReservaFixture(estado = EstadoReserva.CONFIRMADA)

            reserva.cancelar("Calamidad")

            assertEquals(EstadoReserva.CANCELADA, reserva.estado)
        }

        @Test
        fun `should reject cancelling a reservation that is not pending or confirmed`() {
            listOf(EstadoReserva.EN_CURSO, EstadoReserva.CANCELADA, EstadoReserva.FINALIZADA, EstadoReserva.NO_SHOW)
                .forEach { estado ->
                    val reserva = ReservaFixture(estado = estado)

                    assertThrows(ReglaDominioException::class.java) { reserva.cancelar("Motivo") }
                    assertEquals(estado, reserva.estado)
                }
        }

        @Test
        fun `should reject a blank reason and leave the reservation untouched`() {
            val reserva = ReservaFixture(estado = EstadoReserva.PENDIENTE)

            assertThrows(ReglaDominioException::class.java) { reserva.cancelar("   ") }

            assertEquals(EstadoReserva.PENDIENTE, reserva.estado)
        }
    }

    @Nested
    inner class Modificar {
        private val inicio = LocalDate.now().plusDays(20)

        @Test
        fun `should replace the editable data and keep identity, state and frozen policy`() {
            val reserva = ReservaFixture(estado = EstadoReserva.CONFIRMADA)
            val nuevaEstancia = Estancia(Periodo(inicio, inicio.plusDays(4)))
            val nuevaHora = HoraLlegada(LocalTime.of(18, 30))
            val nuevoValor = Dinero(500.0)
            val id = reserva.id
            val codigo = reserva.codigo
            val politica = reserva.politica

            reserva.modificar(reserva.apartamento, nuevaEstancia, reserva.ocupantes, nuevaHora, nuevoValor)

            assertEquals(nuevaEstancia, reserva.estancia)
            assertEquals(nuevaHora, reserva.horaLlegada)
            assertEquals(nuevoValor, reserva.valor)
            assertEquals(id, reserva.id)
            assertEquals(codigo, reserva.codigo)
            assertEquals(EstadoReserva.CONFIRMADA, reserva.estado)
            assertEquals(politica, reserva.politica)
        }

        @Test
        fun `should reject a stay that starts before today without changing the reservation`() {
            val reserva = ReservaFixture()
            val estanciaOriginal = reserva.estancia
            val pasada = Estancia(Periodo(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1)))

            assertThrows(ReglaDominioException::class.java) {
                reserva.modificar(reserva.apartamento, pasada, reserva.ocupantes, reserva.horaLlegada, reserva.valor)
            }

            assertEquals(estanciaOriginal, reserva.estancia)
        }

        @Test
        fun `should reject more occupants than the apartment admits without changing the reservation`() {
            val reserva = ReservaFixture(apartamento = ApartamentoFixture())
            val demasiados = List(11) { OcupanteFixture() }

            assertThrows(ReglaDominioException::class.java) {
                reserva.modificar(reserva.apartamento, reserva.estancia, demasiados, reserva.horaLlegada, reserva.valor)
            }

            assertEquals(1, reserva.ocupantes.size)
        }

        @Test
        fun `should reject modifying a reservation that is not pending or confirmed`() {
            listOf(EstadoReserva.EN_CURSO, EstadoReserva.CANCELADA, EstadoReserva.FINALIZADA, EstadoReserva.NO_SHOW)
                .forEach { estado ->
                    val reserva = ReservaFixture(estado = estado)

                    assertThrows(ReglaDominioException::class.java) {
                        reserva.modificar(
                            reserva.apartamento,
                            reserva.estancia,
                            reserva.ocupantes,
                            reserva.horaLlegada,
                            reserva.valor,
                        )
                    }
                }
        }
    }

    @Nested
    inner class ValidarValor {
        @Test
        fun `should reject a negative reservation value`() {
            assertThrows(ReglaDominioException::class.java) { ReservaFixture(valor = -1.0) }
        }
    }

    @Nested
    inner class ValidarEstancia {
        @Test
        fun `should reject a stay that starts before today`() {
            val estanciaPasada = Estancia(Periodo(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1)))

            assertThrows(ReglaDominioException::class.java) { ReservaFixture(estancia = estanciaPasada) }
        }
    }
}
