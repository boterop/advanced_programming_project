package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PaginaTest {
    private val elementos = (1..25).toList()

    @Test
    fun `should slice the requested page in blocks of ten`() {
        val pagina = Pagina.de(elementos, 3)

        assertEquals((21..25).toList(), pagina.contenido)
        assertEquals(25L, pagina.totalElementos)
        assertEquals(3, pagina.totalPaginas)
        assertEquals(Pagina.TAMANO, pagina.tamano)
    }

    @Test
    fun `should return an empty page when the number is beyond the last one`() {
        val pagina = Pagina.de(elementos, 9)

        assertTrue(pagina.contenido.isEmpty())
        assertEquals(25L, pagina.totalElementos)
    }

    @Test
    fun `should reject page numbers below one`() {
        assertThrows(ReglaDominioException::class.java) { Pagina.de(elementos, 0) }
    }
}