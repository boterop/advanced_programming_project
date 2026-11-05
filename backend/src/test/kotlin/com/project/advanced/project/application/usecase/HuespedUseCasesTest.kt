package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.ConsultarHistorialHuespedRequest
import com.project.advanced.project.application.dto.request.OcupanteRequest
import com.project.advanced.project.application.dto.request.RegistrarOcupanteRequest
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.service.CotizarEstanciaService
import com.project.advanced.project.domain.service.RegistroOcupanteService
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import com.project.advanced.project.fixtures.ApartamentoFixture
import com.project.advanced.project.fixtures.OcupanteFixture
import com.project.advanced.project.fixtures.ReservaFixture
import com.project.advanced.project.infrastructure.persistence.memory.FolioRepositoryMemory
import com.project.advanced.project.infrastructure.persistence.memory.ReservaRepositoryMemory
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class HuespedUseCasesTest {private val reservas = ReservaRepositoryMemory(1L)
    private val folios = FolioRepositoryMemory()
    private val registrarOcupante =
        RegistrarOcupanteUseCase(reservas, folios,
            RegistroOcupanteService(CotizarEstanciaService()))
    private val historial = ConsultarHistorialHuespedUseCase(reservas)

    private fun nuevoId() = ID(UUID.randomUUID().toString())

    private fun reservaDe(
        documento: String,
        diasDesdeHoy: Long = 5,
        estado: EstadoReserva = EstadoReserva.CONFIRMADA,
    ): Reserva {
        val apartamento = ApartamentoFixture(tarifas = Temporada.entries.map {
            Tarifa(it, Dinero(100.0))
        })
        val entrada = LocalDate.now().plusDays(diasDesdeHoy)
        return ReservaFixture(
            apartamento = apartamento,
            estancia = Estancia(Periodo(entrada, entrada.plusDays(3))),
            titular = OcupanteFixture(documento = documento),
            estado = estado,
            valor = 300.0,
        ).also { reservas.guardar(it) }
    }

    private fun conFolio(reserva: Reserva) {
        val alojamiento = Cargo.crear(nuevoId(), ConceptoCargo.ALOJAMIENTO, Dinero(300.0))
        folios.guardar(Folio.crear(reserva.id, mutableListOf(alojamiento), mutableListOf()))
    }

    private fun ocupanteCommand(documento: String = "ACOMP001") =
        OcupanteRequest(
            documento,
            "Ana Perez",
            "ana@correo.com",
            LocalDate.of(1992, 3, 3)
        )

    @Test
    fun `should register a companion, persist it and report the adjustment`() {
        val reserva = reservaDe("111")
        conFolio(reserva)

        val resultado = registrarOcupante.ejecutar(
            RegistrarOcupanteRequest(reserva.codigo.valor, ocupanteCommand())
        )

        assertEquals(2, resultado.totalOcupantes)
        assertEquals(0, BigDecimal.valueOf(600.0).compareTo(resultado.valorReserva))
        assertEquals(0, BigDecimal.valueOf(300.0).compareTo(resultado.ajuste))
        assertEquals(2, reservas.buscarPorCodigo(reserva.codigo)!!.ocupantes.size)
        assertEquals(2, folios.buscar(reserva.id)!!.listarCargos().size)
    }

    @Test
    fun `should fail when the reservation does not exist`() {
        assertThrows(ReservaNoEncontradaException::class.java) {
            registrarOcupante.ejecutar(RegistrarOcupanteRequest(
                UUID.randomUUID().toString(), ocupanteCommand())
            )
        }
    }

    @Test
    fun `should fail when the reservation has no folio`() {
        val reserva = reservaDe("111")

        assertThrows(ReglaDominioException::class.java) {
            registrarOcupante.ejecutar(RegistrarOcupanteRequest(
                reserva.codigo.valor, ocupanteCommand())
            )
        }
    }

    @Test
    fun `should list every reservation of the holder in any state, newest stay first`() {
        val proxima = reservaDe("111", diasDesdeHoy = 5)
        val lejana = reservaDe("111", diasDesdeHoy = 40)
        val cancelada = reservaDe("111", diasDesdeHoy = 20, estado = EstadoReserva.CANCELADA)
        reservaDe("222")

        val pagina = historial.ejecutar(ConsultarHistorialHuespedRequest("111"))

        assertEquals(
            listOf(lejana.codigo.valor, cancelada.codigo.valor, proxima.codigo.valor),
            pagina.contenido.map { it.codigo },
        )
        assertEquals(EstadoReserva.CANCELADA, pagina.contenido[1].estado)
        assertEquals(3L, pagina.totalElementos)
    }

    @Test
    fun `should not include reservations where the person is only a companion`() {
        val ajena = reservaDe("222")
        reservas.guardar(
            ReservaFixture(
                apartamento = ajena.apartamento,
                titular = OcupanteFixture(documento = "333"),
                ocupantes = listOf(OcupanteFixture(documento = "333"), OcupanteFixture(documento = "111")),
            ),
        )

        assertTrue(historial.ejecutar(ConsultarHistorialHuespedRequest("111")).contenido.isEmpty())
    }

    @Test
    fun `should paginate the history in blocks of ten`() {
        repeat(12) { reservaDe("111", diasDesdeHoy = 5L + it) }

        val primera = historial.ejecutar(ConsultarHistorialHuespedRequest("111", pagina = 1))
        val segunda = historial.ejecutar(ConsultarHistorialHuespedRequest("111", pagina = 2))

        assertEquals(10, primera.contenido.size)
        assertEquals(2, segunda.contenido.size)
        assertEquals(2, primera.totalPaginas)
        assertEquals(12L, primera.totalElementos)
    }

    @Test
    fun `should return an empty page for a document without reservations`() {
        val pagina = historial.ejecutar(ConsultarHistorialHuespedRequest("999"))

        assertTrue(pagina.contenido.isEmpty())
        assertEquals(0L, pagina.totalElementos)
    }

    @Test
    fun `should reject a page number below one`() {
        assertThrows(ReglaDominioException::class.java) {
            historial.ejecutar(ConsultarHistorialHuespedRequest("111", pagina = 0))
        }
    }
}