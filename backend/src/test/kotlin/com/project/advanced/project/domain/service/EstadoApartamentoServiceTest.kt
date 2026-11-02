package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.ApartamentoFixture.invoke
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class EstadoApartamentoServiceTest {
    private val servicio = EstadoOperativoApartamentoService()

    private fun apartamentoEn(estado: EstadoApartamento) = ApartamentoFixture().also { it.estado = estado }

    @Test
    fun `should walk PENDIENTE_PREPARACION to EN_PREPARACION to PREPARADO`() {
        val apartamento = apartamentoEn(EstadoApartamento.PENDIENTE_PREPARACION)
        servicio.cambiar(apartamento, EstadoApartamento.EN_PREPARACION)
        assertEquals(EstadoApartamento.EN_PREPARACION, apartamento.estado)
        servicio.cambiar(apartamento, EstadoApartamento.PREPARADO)
        assertEquals(EstadoApartamento.PREPARADO, apartamento.estado)
    }

    @Test
    fun `should reject transitions that skip a step or belong to check-in and check-out`() {
        listOf(
            EstadoApartamento.PENDIENTE_PREPARACION to EstadoApartamento.PREPARADO,
            EstadoApartamento.PREPARADO to EstadoApartamento.EN_PREPARACION,
            EstadoApartamento.PREPARADO to EstadoApartamento.OCUPADO,
            EstadoApartamento.OCUPADO to EstadoApartamento.PENDIENTE_PREPARACION,
            EstadoApartamento.OCUPADO to EstadoApartamento.PREPARADO,
            EstadoApartamento.EN_PREPARACION to EstadoApartamento.PENDIENTE_PREPARACION,
        ).forEach { (desde, hacia) ->
            val apartamento = apartamentoEn(desde)
            assertThrows(ReglaDominioException::class.java) { servicio.cambiar(apartamento, hacia) }
            assertEquals(desde, apartamento.estado)
        }
    }

    @Test
    fun `should never let service staff declare an apartment out of service`() {
        EstadoApartamento.entries.forEach { desde ->
            val apartamento = apartamentoEn(desde)
            assertThrows(ReglaDominioException::class.java) {
                servicio.cambiar(apartamento, EstadoApartamento.FUERA_DE_SERVICIO)
            }
        }
    }
}