package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MotivoBloqueo
import com.project.advanced.project.domain.valueobject.Periodo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.UUID

class BloqueoTest {
    private val apartamentoId = ID(UUID.randomUUID().toString())
    private val inicio = LocalDate.of(2030, 6, 10)
    private val periodo = Periodo(inicio, inicio.plusDays(5))
    private val bloqueo = Bloqueo.crear(apartamentoId, periodo, MotivoBloqueo.USO_INTERNO, "Uso interno")

    @Nested
    inner class Crear {
        @Test
        fun `should create a vigente bloqueo`() {
            val bloqueo = Bloqueo.crear(apartamentoId, periodo, MotivoBloqueo.MANTENIMIENTO, "Reparaciones")

            assertEquals(apartamentoId, bloqueo.apartamentoId)
            assertEquals(periodo, bloqueo.periodo)
            assertEquals(MotivoBloqueo.MANTENIMIENTO, bloqueo.motivo)
            assertEquals("Reparaciones", bloqueo.observacion)
            assertTrue(bloqueo.vigente)
        }
    }

    @Nested
    inner class Cubre {
        @Test
        fun `should cover a night inside its period while vigente`() {
            assertTrue(bloqueo.cubre(inicio.plusDays(2)))
        }

        @Test
        fun `should not cover nights outside its period`() {
            assertFalse(bloqueo.cubre(inicio))
            assertFalse(bloqueo.cubre(inicio.plusDays(5)))
        }
    }

    @Nested
    inner class Levantar {
        @Test
        fun `should mark the bloqueo as not vigente`() {
            bloqueo.levantar()

            assertFalse(bloqueo.vigente)
            assertFalse(bloqueo.cubre(inicio.plusDays(2)))
        }
    }
}
