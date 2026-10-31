package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.fixtures.ReservaFixture.invoke
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class VencimientoReservaServiceTest {
    private val servicio = VencimientoReservaService()
    private val hoy = LocalDate.now()

    @Test
    fun `should expire a pending reservation once the confirmation window has passed`() {
        val reserva = ReservaFixture(estado = EstadoReserva.PENDIENTE)

        assertTrue(servicio.estaVencida(reserva, hoy.plusDays(2), plazoConfirmacionDias = 1))
    }

    @Test
    fun `should not expire a pending reservation still inside the window`() {
        val reserva = ReservaFixture(estado = EstadoReserva.PENDIENTE)

        assertFalse(servicio.estaVencida(reserva, hoy.plusDays(1), plazoConfirmacionDias = 1))
        assertFalse(servicio.estaVencida(reserva, hoy, plazoConfirmacionDias = 1))
    }

    @Test
    fun `should never expire a reservation that is not pending`() {
        listOf(EstadoReserva.CONFIRMADA, EstadoReserva.EN_CURSO, EstadoReserva.CANCELADA).forEach { estado ->
            val reserva = ReservaFixture(estado = estado)

            assertFalse(servicio.estaVencida(reserva, hoy.plusDays(30), plazoConfirmacionDias = 1))
        }
    }
}