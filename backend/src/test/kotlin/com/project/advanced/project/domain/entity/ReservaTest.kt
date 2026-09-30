package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.CorreoElectronico
import com.project.advanced.project.domain.valueobject.Descripcion
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.DocumentoIdentidad
import com.project.advanced.project.domain.valueobject.Dormitorio
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
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime

class ReservaTest {
    private val fechaInicio = LocalDate.now().plusDays(10)
    private val apartamento =
        Apartamento.crear(
            IdApartamento("apt-1"),
            Nombre("Apartamento"),
            listOf(Dormitorio(Nombre("Dormitorio 1"), Descripcion("Dormitorio central"))),
            Capacidad(10),
            EstadoApartamento.PREPARADO,
            true,
            emptyList(),
        )
    private val titular =
        Ocupante.crear(
            DocumentoIdentidad("123456789"),
            Nombre("Jhon Doe"),
            CorreoElectronico("jhon.doe@gmail.com"),
            FechaNacimiento(LocalDate.of(1990, 1, 1)),
        )

    private fun crearReserva(
        estancia: Estancia = Estancia(Periodo(fechaInicio, fechaInicio.plusDays(2))),
        valor: Double = 200.0,
    ) = Reserva.crear(
        CodigoReserva("RES-001"),
        apartamento,
        estancia,
        EstadoReserva.CONFIRMADA,
        CanalOrigen.DIRECTO,
        IdExterno(CanalOrigen.DIRECTO, "ext-1"),
        titular,
        listOf(titular),
        HoraLlegada(LocalTime.of(15, 0)),
        Dinero(valor),
        VersionPolitica(1, LocalDate.now()),
        "",
    )

    @Nested
    inner class Crear {
        @Test
        fun `should create a reserva with its supplied values`() {
            val reserva = crearReserva()

            assertEquals(CodigoReserva("RES-001"), reserva.codigo)
            assertEquals(apartamento, reserva.apartamento)
            assertEquals(Estancia(Periodo(fechaInicio, fechaInicio.plusDays(2))), reserva.estancia)
            assertEquals(EstadoReserva.CONFIRMADA, reserva.estado)
            assertEquals(CanalOrigen.DIRECTO, reserva.canalOrigen)
            assertEquals(IdExterno(CanalOrigen.DIRECTO, "ext-1"), reserva.idExterno)
            assertEquals(titular, reserva.titular)
            assertEquals(listOf(titular), reserva.ocupantes)
            assertEquals(HoraLlegada(LocalTime.of(15, 0)), reserva.horaLlegada)
            assertEquals(Dinero(200.0), reserva.valor)
            assertEquals(VersionPolitica(1, LocalDate.now()), reserva.politica)
            assertEquals(LocalDate.now(), reserva.fechaCreacion.valor)
            assertEquals("", reserva.motivoCancelacion)
        }
    }

    @Nested
    inner class ValidarValor {
        @Test
        fun `should reject a negative reservation value`() {
            assertThrows(ReglaDominioException::class.java) { crearReserva(valor = -1.0) }
        }
    }

    @Nested
    inner class ValidarEstancia {
        @Test
        fun `should reject a stay that starts before today`() {
            val estanciaPasada = Estancia(Periodo(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1)))

            assertThrows(ReglaDominioException::class.java) { crearReserva(estancia = estanciaPasada) }
        }
    }
}
