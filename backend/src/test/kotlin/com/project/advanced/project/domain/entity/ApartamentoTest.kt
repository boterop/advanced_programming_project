package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.IdTemporada
import com.project.advanced.project.domain.valueobject.Tarifa
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ApartamentoTest {
    private val id = IdApartamento(" apt-1 ")
    private val tarifas = listOf(Tarifa(IdTemporada("verano"), 100.0))
    private val apartamento = Apartamento.crear(id, "Apartamento central", 2, 4, EstadoApartamento.PREPARADO, true, tarifas)

    @Nested
    inner class Crear {
        @Test
        fun `should create an apartamento in prepared and active state`() {
            val apartamento = Apartamento.crear(
                id,
                "Apartamento central",
                2,
                4,
                EstadoApartamento.FUERA_DE_SERVICIO,
                false,
                tarifas,
            )

            assertEquals(id, apartamento.id)
            assertEquals("Apartamento central", apartamento.nombre)
            assertEquals(2, apartamento.dormitorios)
            assertEquals(4, apartamento.capacidad)
            assertEquals(EstadoApartamento.PREPARADO, apartamento.estado)
            assertTrue(apartamento.activo)
            assertEquals(tarifas, apartamento.tarifas)
        }
    }

    @Nested
    inner class Admite {
        @Test
        fun `should admit occupants up to its capacity`() {
            assertTrue(apartamento.admite(4))
            assertTrue(apartamento.admite(2))
        }

        @Test
        fun `should not admit more occupants than its capacity`() {
            assertFalse(apartamento.admite(5))
        }
    }

    @Nested
    inner class EsActivo {
        @Test
        fun `should return true when the apartamento is active`() {
            assertTrue(apartamento.esActivo())
        }

        @Test
        fun `should return false when the apartamento is inactive`() {
            apartamento.activo = false

            assertFalse(apartamento.esActivo())
        }
    }
}
