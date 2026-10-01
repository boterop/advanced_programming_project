package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.OcupanteFixture
import com.project.advanced.project.fixtures.ReservaFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import java.time.LocalDate

class ReservaRepositoryMemoryTest {
    private val fechaInicio = LocalDate.now().plusDays(10)
    private val apartamento = ApartamentoFixture()
    private val apartamentoAlternativo = ApartamentoFixture(nombre = Nombre("apto-2"))
    private val titular = OcupanteFixture()

    @Nested
    inner class Listar {
        @Test
        fun `should return all reservations`() {
            val repository = ReservaRepositoryMemory()
            val reserva = ReservaFixture()

            repository.guardar(reserva)

            assertEquals(listOf(reserva), repository.listar())
        }
    }

    @Nested
    inner class Buscar {
        @Test
        fun `should return the reservation`() {
            val repository = ReservaRepositoryMemory()
            val reserva = ReservaFixture()

            repository.guardar(reserva)

            assertEquals(reserva, repository.buscar(reserva.id))
        }

        @Test
        fun `should return null when the reservation does not exist`() {
            val repository = ReservaRepositoryMemory()

            assertNull(repository.buscar(ID("2a79549d-4526-46b3-a4a4-fbde0b435352")))
        }
    }

    @Nested
    inner class BuscarActivasPorApartamento {
        val periodo = Periodo(fechaInicio.plusDays(1), fechaInicio.plusDays(2))

        val estancia = Estancia(periodo)

        @Test
        fun `should return reservations for the apartment whose stays overlap the requested period`() {
            val repository = ReservaRepositoryMemory()

            val reservaCoincidente = ReservaFixture(apartamento = apartamento, estancia = estancia)
            val reservaMismoApartamento = ReservaFixture(apartamento = apartamento)
            val reservaOtroApartamento = ReservaFixture(apartamento = apartamentoAlternativo)

            val resultado =
                repository
                    .also {
                        it.guardar(reservaCoincidente)
                        it.guardar(reservaMismoApartamento)
                        it.guardar(reservaOtroApartamento)
                    }.buscarActivasPorApartamento(apartamento.id, periodo)

            assertEquals(listOf(reservaCoincidente), resultado)
        }

        @Test
        fun `should include reservations when the requested period is contained in their stay`() {
            val repository = ReservaRepositoryMemory()
            val reserva = ReservaFixture(apartamento = apartamento, estancia = estancia)

            repository.guardar(reserva)

            assertEquals(
                listOf(reserva),
                repository.buscarActivasPorApartamento(
                    apartamento.id,
                    periodo,
                ),
            )
        }

        @Test
        fun `should return an empty list when no reservations overlap for the apartment`() {
            val repository = ReservaRepositoryMemory()
            val reserva = ReservaFixture(apartamento = apartamento)
            repository.guardar(reserva)

            val resultado =
                repository.buscarActivasPorApartamento(
                    apartamento.id,
                    periodo,
                )

            assertEquals(emptyList<Reserva>(), resultado)
        }
    }

    @Nested
    inner class Guardar {
        @Test
        fun `should save the reservation`() {
            val repository = ReservaRepositoryMemory()
            val reserva = ReservaFixture()

            repository.guardar(reserva)

            assertEquals(reserva, repository.buscar(reserva.id))
        }
    }

    @Nested
    inner class Eliminar {
        @Test
        fun `should remove the reservation`() {
            val repository = ReservaRepositoryMemory()
            val reserva = ReservaFixture()

            repository.guardar(reserva)
            repository.eliminar(reserva)

            assertNull(repository.buscar(reserva.id))
        }
    }
}
