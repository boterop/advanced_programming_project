package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.valueobject.Periodo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate

@SpringBootTest
class EstanciaTest {
    private val inicio = LocalDate.now()
    private val fin = inicio.plusDays(10)
    private val periodo = Periodo(inicio, fin)
    private val estancia = Estancia(periodo)

    @Nested
    inner class CalcularValor {
        val documento = DocumentoIdentidad("123456789")
        val nombre = Nombre("Jhon Doe")
        val correo = CorreoElectronico("jhon.doe@gmail.com")

        @Test
        fun `should calculate the correct value`() {
            val fechaNacimientoFacturable = FechaNacimiento(LocalDate.now().minusYears(30))
            val fechaNacimientoNoFacturable = FechaNacimiento(LocalDate.now().minusYears(2))

            val ocupantes =
                listOf(
                    Ocupante.crear(documento, nombre, correo, fechaNacimientoNoFacturable),
                    Ocupante.crear(documento, nombre, correo, fechaNacimientoFacturable),
                    Ocupante.crear(documento, nombre, correo, fechaNacimientoNoFacturable),
                    Ocupante.crear(documento, nombre, correo, fechaNacimientoFacturable),
                    Ocupante.crear(documento, nombre, correo, fechaNacimientoNoFacturable),
                )
            val tarifa = 10.0
            val valor = estancia.calcularValor(ocupantes, tarifa)

            assertEquals(200.0, valor)
        }
    }
}
