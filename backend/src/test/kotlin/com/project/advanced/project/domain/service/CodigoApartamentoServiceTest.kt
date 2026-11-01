package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.Dormitorio
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.ApartamentoFixture.invoke
import com.project.advanced.project.infrastructure.persistence.memory.ApartamentoRepositoryMemory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.UUID

class CodigoApartamentoServiceTest {
    private val repositorio = ApartamentoRepositoryMemory()
    private val servicio = CatalogoApartamentoService(repositorio)
    private val dormitorios = listOf(Dormitorio(Nombre("Principal"), Descripcion("Cama doble")))
    private val tarifasCompletas = Temporada.entries.map { Tarifa(it, Dinero(50000.0)) }

    private fun crear(
        nombre: String = "Apto Mar",
        capacidad: Int = 4,
        tarifas: List<Tarifa> = tarifasCompletas,
    ) = servicio.crear(ID(UUID.randomUUID().toString()), Nombre(nombre), dormitorios, Capacidad(capacidad), tarifas)

    @Test
    fun `should create an active prepared apartment with the given data`() {
        val apartamento = crear()
        assertTrue(apartamento.esActivo())
        assertEquals(EstadoApartamento.PREPARADO, apartamento.estado)
        assertEquals(4, apartamento.capacidad.valor)
        assertEquals(Temporada.entries.size, apartamento.tarifas.size)
    }

    @Test
    fun `should reject a capacity below one`() {
        assertThrows(ReglaDominioException::class.java) { crear(capacidad = 0) }
    }

    @Test
    fun `should reject a name already used, ignoring case`() {
        repositorio.guardar(ApartamentoFixture(nombre = Nombre("Apto Mar")))
        assertThrows(ReglaDominioException::class.java) { crear(nombre = "APTO MAR") }
    }

    @Test
    fun `should reject incomplete tariffs`() {
        assertThrows(ReglaDominioException::class.java) { crear(tarifas = tarifasCompletas.drop(1)) }
    }

    @Test
    fun `should reject two tariffs for the same season`() {
        assertThrows(ReglaDominioException::class.java) { crear(tarifas = tarifasCompletas + tarifasCompletas.first()) }
    }

    @Test
    fun `should reject a zero tariff`() {
        val conCero = tarifasCompletas.drop(1) + Tarifa(tarifasCompletas.first().temporada, Dinero(0))
        assertThrows(ReglaDominioException::class.java) { crear(tarifas = conCero) }
    }
}