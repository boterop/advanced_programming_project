package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Periodo
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PeriodoTest {
    private val inicio = LocalDate.now()
    private val fin = inicio.plusDays(10)
    private val periodo = Periodo(inicio, fin)

    @Nested
    inner class Solapa {
        @Test
        fun `should create a new periodo with valid dates`() {
            assertDoesNotThrow({ Periodo(inicio, fin) })
        }

        @Test
        fun `shouldn't create a new periodo with same dates`() {
            assertThrows(ReglaDominioException::class.java, { Periodo(inicio, inicio) })
        }

        @Test
        fun `should throw an exception if the start date is after the end date`() {
            val fin2 = inicio.minusDays(1)

            assertThrows(ReglaDominioException::class.java, { Periodo(inicio, fin2) })
        }

        @Test
        fun `should return true if the end date is overlaping`() {
            val inicio2 = inicio.minusDays(3)
            val fin2 = inicio.plusDays(1)
            val isOverlaping = periodo.solapa(inicio2, fin2)

            assertTrue(isOverlaping)
        }

        @Test
        fun `should return true if both dates are overlaping`() {
            val inicio2 = inicio.plusDays(1)
            val fin2 = fin.minusDays(1)
            val isOverlaping = periodo.solapa(inicio2, fin2)

            assertTrue(isOverlaping)
        }

        @Test
        fun `should return true if the entire first periodo is overlaping`() {
            val inicio2 = inicio.minusDays(1)
            val fin2 = fin.plusDays(1)
            val isOverlaping = periodo.solapa(inicio2, fin2)

            assertTrue(isOverlaping)
        }

        @Test
        fun `shouldn't overlap two consecutive 1-2 periodos`() {
            val inicio2 = fin
            val fin2 = inicio2.plusDays(1)
            val periodo2 = Periodo(inicio2, fin2)
            val isOverlaping = periodo.solapa(periodo2)

            assertFalse(isOverlaping)
        }

        @Test
        fun `shouldn't overlap two consecutive 2-1 dates`() {
            val inicio2 = inicio.minusDays(3)
            val fin2 = inicio

            val isOverlaping = periodo.solapa(inicio2, fin2)
            assertFalse(isOverlaping)
        }

        @Test
        fun `shouldn't overlap with two different dates`() {
            val inicio2 = fin.plusDays(10)
            val fin2 = inicio2.plusDays(1)
            val isOverlaping = periodo.solapa(inicio2, fin2)

            assertFalse(isOverlaping)
        }
    }
}
