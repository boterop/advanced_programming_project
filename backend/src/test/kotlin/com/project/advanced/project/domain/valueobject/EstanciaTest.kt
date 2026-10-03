package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import com.project.advanced.project.fixtures.OcupanteFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class EstanciaTest {
    private val inicio = LocalDate.now()
    private val fin = inicio.plusDays(10)
    private val periodo = Periodo(inicio, fin)
    private val estancia = Estancia(periodo)

    @Nested
    inner class CalcularValor {
        @Test
        fun `should calculate the correct value`() {
            val fechaNacimientoFacturable = LocalDate.now().minusYears(30)
            val fechaNacimientoNoFacturable = LocalDate.now().minusYears(2)

            val ocupantes =
                listOf(
                    OcupanteFixture(fechaNacimiento = fechaNacimientoNoFacturable),
                    OcupanteFixture(fechaNacimiento = fechaNacimientoFacturable),
                    OcupanteFixture(fechaNacimiento = fechaNacimientoNoFacturable),
                    OcupanteFixture(fechaNacimiento = fechaNacimientoFacturable),
                    OcupanteFixture(fechaNacimiento = fechaNacimientoNoFacturable),
                )
            val tarifa = Tarifa(Temporada.ALTA, Dinero(10.0))
            val valor = estancia.calcularValor(ocupantes, tarifa)

            assertEquals(Dinero(200.0), valor)
        }
    }
}
