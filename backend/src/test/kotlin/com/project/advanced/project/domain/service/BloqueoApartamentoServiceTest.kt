package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MotivoBloqueo
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.ApartamentoFixture.invoke
import com.project.advanced.project.fixtures.BloqueoFixture
import com.project.advanced.project.fixtures.BloqueoFixture.invoke
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.fixtures.ReservaFixture.invoke
import com.project.advanced.project.infrastructure.persistence.memory.ReservaRepositoryMemory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class BloqueoApartamentoServiceTest {
    private val reservas = ReservaRepositoryMemory(1L)
    private val servicio = BloqueoApartamentoService(reservas)
    private val apartamento = ApartamentoFixture()
    private val inicio = LocalDate.now().plusDays(10)
    private val periodo = Periodo(inicio, inicio.plusDays(3))

    private fun nuevoId() = ID(UUID.randomUUID().toString())

    @Nested
    inner class Bloquear {
        @Test
        fun `should create a valid block for a free period`() {
            val bloqueo = servicio.bloquear(nuevoId(), apartamento, periodo, MotivoBloqueo.MANTENIMIENTO, "  Cambio de tuberia  ")
            assertTrue(bloqueo.vigente)
            assertEquals(apartamento.id, bloqueo.apartamentoId)
            assertEquals(periodo, bloqueo.periodo)
            assertEquals(MotivoBloqueo.MANTENIMIENTO, bloqueo.motivo)
            assertEquals("Cambio de tuberia", bloqueo.observacion)
        }

        @Test
        fun `should reject blocking nights that already have an active reservation`() {
            reservas.guardar(
                ReservaFixture(
                    apartamento = apartamento,
                    estancia = Estancia(Periodo(inicio.plusDays(1), inicio.plusDays(2))),
                    estado = EstadoReserva.CONFIRMADA,
                ),
            )
            assertThrows(ReglaDominioException::class.java) {
                servicio.bloquear(nuevoId(), apartamento, periodo, MotivoBloqueo.MANTENIMIENTO, "")
            }
        }
    }

    @Nested
    inner class Desbloquear {
        @Test
        fun `should lift an active block`() {
            val bloqueo = BloqueoFixture(apartamentoId = apartamento.id)
            servicio.desbloquear(bloqueo)
            assertFalse(bloqueo.vigente)
        }

        @Test
        fun `should reject lifting a block that is no longer active`() {
            val bloqueo = BloqueoFixture(apartamentoId = apartamento.id)
            servicio.desbloquear(bloqueo)
            assertThrows(ReglaDominioException::class.java) { servicio.desbloquear(bloqueo) }
        }
    }
}