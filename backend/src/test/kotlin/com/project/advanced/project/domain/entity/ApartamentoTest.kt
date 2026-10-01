package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.Dormitorio
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ApartamentoTest {
    private val id = IdApartamento(" apt-1 ")
    private val tarifas = listOf(Tarifa(Temporada.ALTA, Dinero(100.0)), Tarifa(Temporada.BAJA, Dinero(10.0)))
    private val dormitorios =
        listOf(
            Dormitorio(Nombre("Dormitorio 1"), Descripcion("Dormitorio central")),
            Dormitorio(Nombre("Dormitorio 2"), Descripcion("Dormitorio lateral")),
        )
    private val apartamento =
        Apartamento.crear(
            id,
            Nombre("Apartamento central"),
            dormitorios,
            Capacidad(4),
            EstadoApartamento.PREPARADO,
            true,
            tarifas,
        )

    @Nested
    inner class Crear {
        @Test
        fun `should create an apartamento in prepared and active state`() {
            val apartamento =
                Apartamento.crear(
                    id,
                    Nombre("Apartamento central"),
                    dormitorios,
                    Capacidad(4),
                    EstadoApartamento.FUERA_DE_SERVICIO,
                    false,
                    tarifas,
                )

            assertEquals(id, apartamento.id)
            assertEquals(Nombre("Apartamento central"), apartamento.nombre)
            assertEquals(2, apartamento.dormitorios.size)
            assertEquals(4, apartamento.capacidad.valor)
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
