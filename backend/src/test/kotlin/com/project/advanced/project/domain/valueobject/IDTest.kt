package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.util.UUID

class IDTest {
    @Test
    fun `should create an ID with a valid UUID`() {
        val valor = UUID.randomUUID().toString()

        assertEquals(valor, ID(valor).valor)
    }

    @Test
    fun `should trim whitespace around a valid UUID`() {
        val valor = UUID.randomUUID().toString()

        assertEquals(valor, ID("  $valor  ").valor)
    }

    @Test
    fun `should reject a blank ID`() {
        assertThrows(ReglaDominioException::class.java) { ID("   ") }
    }

    @Test
    fun `should reject a value that is not a UUID`() {
        assertThrows(ReglaDominioException::class.java) { ID("no-es-un-uuid") }
    }

    @Test
    fun `should be equal to another ID with the same value`() {
        val valor = UUID.randomUUID().toString()

        assertEquals(ID(valor), ID(valor))
    }

    @Test
    fun `should not be equal to an ID with a different value`() {
        assertNotEquals(ID(UUID.randomUUID().toString()), ID(UUID.randomUUID().toString()))
    }

    @Test
    fun `should use the value for its hash code`() {
        val valor = UUID.randomUUID().toString()

        assertEquals(valor.hashCode(), ID(valor).hashCode())
    }
}
