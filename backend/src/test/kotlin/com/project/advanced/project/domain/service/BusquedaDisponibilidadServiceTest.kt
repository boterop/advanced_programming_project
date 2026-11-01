package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.TiempoPreparacion
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.ApartamentoFixture.invoke
import com.project.advanced.project.fixtures.BloqueoFixture
import com.project.advanced.project.fixtures.BloqueoFixture.invoke
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.fixtures.ReservaFixture.invoke
import com.project.advanced.project.infrastructure.persistence.memory.ApartamentoRepositoryMemory
import com.project.advanced.project.infrastructure.persistence.memory.BloqueoRepositoryMemory
import com.project.advanced.project.infrastructure.persistence.memory.ReservaRepositoryMemory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BusquedaDisponibilidadServiceTest {
    private val apartamentos = ApartamentoRepositoryMemory()
    private val reservas = ReservaRepositoryMemory(1L)
    private val bloqueos = BloqueoRepositoryMemory()
    private val servicio = BusquedaDisponibilidadService(apartamentos,
        DisponibilidadApartamentoService(reservas, bloqueos))
    private val tiempoPreparacion = TiempoPreparacion(0)
    private val entrada = LocalDate.now().plusDays(10)
    private val estancia = Estancia(Periodo(entrada, entrada.plusDays(3)))
    private val nocheInterna = Estancia(Periodo(entrada.plusDays(1),
        entrada.plusDays(2)))

    @Test
    fun `should return free apartments with enough capacity sorted by name`() {
        val b = ApartamentoFixture(nombre = Nombre("B"))
        val a = ApartamentoFixture(nombre = Nombre("A"))
        apartamentos.guardar(b)
        apartamentos.guardar(a)
        val resultado = servicio.buscarDisponibles(estancia, 4, tiempoPreparacion)
        assertEquals(listOf(a, b), resultado)
    }

    @Test
    fun `should exclude apartments that cannot hold the group`() {
        apartamentos.guardar(ApartamentoFixture(capacidad = Capacidad(2)))
        assertTrue(servicio.buscarDisponibles(estancia, 3, tiempoPreparacion).isEmpty())
    }

    @Test
    fun `should exclude inactive apartments`() {
        val apartamento = ApartamentoFixture()
        apartamento.activo = false
        apartamentos.guardar(apartamento)
        assertTrue(servicio.buscarDisponibles(estancia, 2, tiempoPreparacion).isEmpty())
    }

    @Test
    fun `should exclude apartments with an active reservation overlapping the stay`() {
        val ocupado = ApartamentoFixture(nombre = Nombre("Ocupado"))
        val libre = ApartamentoFixture(nombre = Nombre("Libre"))
        apartamentos.guardar(ocupado)
        apartamentos.guardar(libre)
        reservas.guardar(ReservaFixture(apartamento = ocupado, estancia = nocheInterna,
            estado = EstadoReserva.CONFIRMADA))
        assertEquals(listOf(libre), servicio.buscarDisponibles(estancia, 2, tiempoPreparacion))
    }

    @Test
    fun `should keep apartments whose overlapping reservation is no longer active`() {
        val apartamento = ApartamentoFixture()
        apartamentos.guardar(apartamento)
        reservas.guardar(ReservaFixture(apartamento = apartamento, estancia = nocheInterna,
            estado = EstadoReserva.CANCELADA))
        assertEquals(listOf(apartamento), servicio.buscarDisponibles(estancia, 2, tiempoPreparacion))
    }

    @Test
    fun `should exclude apartments with a block overlapping the stay`() {
        val apartamento = ApartamentoFixture()
        apartamentos.guardar(apartamento)
        bloqueos.guardar(BloqueoFixture(apartamentoId = apartamento.id, periodo = nocheInterna.periodo))
        assertTrue(servicio.buscarDisponibles(estancia, 2, tiempoPreparacion).isEmpty())
    }

    @Test
    fun `should reject a group without occupants`() {
        assertThrows(ReglaDominioException::class.java){
            servicio.buscarDisponibles(estancia, 0, tiempoPreparacion)
        }
    }

    @Test
    fun `should reject a stay that starts before today`() {
        val pasada = Estancia(Periodo(LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(1)))
        assertThrows(ReglaDominioException::class.java){
            servicio.buscarDisponibles(pasada, 2, tiempoPreparacion)
        }
    }
}