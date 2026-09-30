package com.project.advanced.project.infrastructure.persistence.memory

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.EstadoApartamento
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.FechaNacimiento
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.IdApartamento
import com.project.advanced.project.domain.valueobject.IdExterno
import com.project.advanced.project.domain.valueobject.Nombre
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.VersionPolitica
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservaRepositoryMemoryTest {
    private val fechaInicio = LocalDate.now().plusDays(10)
    private val apartamento = crearApartamento("apt-1")
    private val apartamentoAlternativo = crearApartamento("apt-2")
    private val titular =
        Ocupante.crear(
            DocumentoIdentidad("123456789"),
            Nombre("Jhon Doe"),
            CorreoElectronico("jhon.doe@gmail.com"),
            FechaNacimiento(LocalDate.of(1990, 1, 1)),
        )

    private fun crearApartamento(id: String) =
        Apartamento.crear(
            IdApartamento(id),
            "Apartamento $id",
            1,
            Capacidad(10),
            EstadoApartamento.PREPARADO,
            true,
            emptyList(),
        )

    private fun crearReserva(
        codigo: String,
        apartamento: Apartamento = this.apartamento,
        inicio: LocalDate = fechaInicio,
        fin: LocalDate = fechaInicio.plusDays(3),
        estado: EstadoReserva = EstadoReserva.CONFIRMADA,
    ) = Reserva.crear(
        CodigoReserva(codigo),
        apartamento,
        Estancia(Periodo(inicio, fin)),
        estado,
        CanalOrigen.DIRECTO,
        IdExterno(CanalOrigen.DIRECTO, codigo),
        titular,
        listOf(titular),
        HoraLlegada(LocalTime.of(15, 0)),
        Dinero(200.0),
        VersionPolitica(1, LocalDate.now()),
        "",
    )

    @Nested
    inner class BuscarActivasPorApartamento {
        @Test
        fun `should return reservations for the apartment whose stays overlap the requested period`() {
            val repository = ReservaRepositoryMemory()
            val reservaCoincidente = crearReserva("RES-001")
            val reservaMismoApartamento =
                crearReserva(
                    "RES-002",
                    inicio = fechaInicio.plusDays(5),
                    fin = fechaInicio.plusDays(7),
                )
            val reservaOtroApartamento =
                crearReserva(
                    "RES-003",
                    apartamento = apartamentoAlternativo,
                )
            val periodoBuscado = Periodo(fechaInicio.plusDays(1), fechaInicio.plusDays(2))
            val resultado =
                repository
                    .also { agregarReservas(it, reservaCoincidente, reservaMismoApartamento, reservaOtroApartamento) }
                    .buscarActivasPorApartamento(apartamento.id, periodoBuscado)

            assertEquals(listOf(reservaCoincidente), resultado)
        }

        @Test
        fun `should include reservations when the requested period is contained in their stay`() {
            val repository = ReservaRepositoryMemory()
            val reserva = crearReserva("RES-004")

            agregarReservas(repository, reserva)

            assertEquals(
                listOf(reserva),
                repository.buscarActivasPorApartamento(
                    apartamento.id,
                    Periodo(fechaInicio.plusDays(1), fechaInicio.plusDays(2)),
                ),
            )
        }

        @Test
        fun `should return an empty list when no reservations overlap for the apartment`() {
            val repository = ReservaRepositoryMemory()
            val reserva = crearReserva("RES-005")
            agregarReservas(repository, reserva)

            val resultado =
                repository.buscarActivasPorApartamento(
                    apartamento.id,
                    Periodo(fechaInicio.plusDays(4), fechaInicio.plusDays(6)),
                )

            assertEquals(emptyList<Reserva>(), resultado)
        }
    }
}

private fun agregarReservas(
    repository: ReservaRepositoryMemory,
    vararg reservas: Reserva,
) {
    val reservasInternas = ReservaRepositoryMemory::class.java.getDeclaredField("reservas")
    reservasInternas.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    val almacen = reservasInternas.get(repository) as HashMap<String, Reserva>
    reservas.forEach { almacen[it.codigo.codigo] = it }
}
