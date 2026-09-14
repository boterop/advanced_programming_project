package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.FechaNacimiento
import com.project.advanced.project.domain.valueobject.Nombre
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

class OcupanteTest {
    val documento = DocumentoIdentidad("123456789")
    val nombre = Nombre("Ana Gómez")
    val correo = CorreoElectronico("ana@example.com")
    val fechaNacimiento = FechaNacimiento(LocalDate.of(1999, 10, 9))

    @Nested
    inner class Crear {
        @Test
        fun `should create a new ocupante`() {
            val ocupante = Ocupante.crear(documento, nombre, correo, fechaNacimiento)

            assertEquals(documento, ocupante.documentoIdentidad)
            assertEquals(nombre, ocupante.nombre)
            assertEquals(correo, ocupante.correoElectronico)
            assertEquals(fechaNacimiento, ocupante.fechaNacimiento)
        }
    }

    @Nested
    inner class EsFacturable {
        val ocupante = Ocupante.crear(documento, nombre, correo, fechaNacimiento)

        @Test
        fun `should return true if the ocupante is facturable`() {
            assertTrue(ocupante.esFacturable(Estancia(LocalDate.now())))
        }

        @Test
        fun `should return false if the ocupante is not facturable`() {
            assertFalse(ocupante.esFacturable(Estancia(fechaNacimiento.valor.plusDays(1))))
        }
    }
}
