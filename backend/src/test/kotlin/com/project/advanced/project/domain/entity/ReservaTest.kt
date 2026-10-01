package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.entity.PoliticaCancelacion
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.IdExterno
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.OcupanteFixture
import com.project.advanced.project.fixtures.PoliticaCancelacionFixture
import com.project.advanced.project.fixtures.ReservaFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class ReservaTest {
    private val politica = PoliticaCancelacionFixture()
    private val fechaInicio = LocalDate.now().plusDays(10)
    private val titular = OcupanteFixture()

    @Nested
    inner class Crear {
        @Test
        fun `should create a reserva with its supplied values`() {
            val id = UUID.randomUUID().toString()
            val apto = ApartamentoFixture()
            val periodo = Periodo(fechaInicio, fechaInicio.plusDays(2))
            val idExterno = IdExterno(CanalOrigen.DIRECTO, "ext-1")
            val horaLlegada = HoraLlegada(LocalTime.of(15, 0))
            val valor = 200.0

            val reserva =
                Reserva.crear(
                    CodigoReserva(id),
                    apto,
                    Estancia(periodo),
                    EstadoReserva.CONFIRMADA,
                    CanalOrigen.DIRECTO,
                    idExterno,
                    titular,
                    listOf(titular),
                    horaLlegada,
                    Dinero(valor),
                    politica,
                    "",
                )

            assertEquals(CodigoReserva(id), reserva.codigo)
            assertEquals(apto, reserva.apartamento)
            assertEquals(Estancia(periodo), reserva.estancia)
            assertEquals(EstadoReserva.CONFIRMADA, reserva.estado)
            assertEquals(CanalOrigen.DIRECTO, reserva.canalOrigen)
            assertEquals(idExterno, reserva.idExterno)
            assertEquals(titular, reserva.titular)
            assertEquals(listOf(titular), reserva.ocupantes)
            assertEquals(horaLlegada, reserva.horaLlegada)
            assertEquals(Dinero(valor), reserva.valor)
            assertEquals(politica, reserva.politica)
            assertEquals(LocalDate.now(), reserva.fechaCreacion.valor)
            assertEquals("", reserva.motivoCancelacion)
        }
    }

    @Nested
    inner class ValidarValor {
        @Test
        fun `should reject a negative reservation value`() {
            assertThrows(ReglaDominioException::class.java) { ReservaFixture(valor = -1.0) }
        }
    }

    @Nested
    inner class ValidarEstancia {
        @Test
        fun `should reject a stay that starts before today`() {
            val estanciaPasada = Estancia(Periodo(LocalDate.now().minusDays(3), LocalDate.now().minusDays(1)))

            assertThrows(ReglaDominioException::class.java) { ReservaFixture(estancia = estanciaPasada) }
        }
    }
}
