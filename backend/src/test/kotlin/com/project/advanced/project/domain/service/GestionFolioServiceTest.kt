package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Pago
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoFolio
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MedioPago
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.UUID

class GestionFolioServiceTest {
    private val servicio = GestionFolioService()

    private fun nuevoId() = ID(UUID.randomUUID().toString())

    private fun folio(
        cargo: Double,
        pagado: Double = 0.0,
    ): Folio {
        val alojamiento = Cargo.crear(nuevoId(), ConceptoCargo.ALOJAMIENTO, Dinero(cargo))
        val folio = Folio.crear(nuevoId(), mutableListOf(alojamiento), mutableListOf())
        if (pagado > 0) folio.registrarPago(Pago.crear(nuevoId(), MedioPago.EFECTIVO, Dinero(pagado)))
        return folio
    }

    @Nested
    inner class RegistrarCargo {
        @Test
        fun `should add a service charge and raise the balance`() {
            val folio = folio(cargo = 200.0)
            servicio.registrarCargo(folio, nuevoId(), ConceptoCargo.SERVICIO, Dinero(30.0))
            assertEquals(0, Dinero(230.0).cantidad().compareTo(folio.saldo().cantidad()))
            assertEquals(2, folio.listarCargos().size)
        }

        @Test
        fun `should accept a penalty`() {
            val folio = folio(cargo = 200.0)
            servicio.registrarCargo(folio, nuevoId(), ConceptoCargo.PENALIDAD, Dinero(50.0))
            assertEquals(2, folio.listarCargos().size)
        }

        @Test
        fun `should reject charges that the system generates by itself`() {
            val folio = folio(cargo = 200.0)

            listOf(ConceptoCargo.ALOJAMIENTO, ConceptoCargo.AJUSTE).forEach { concepto ->
                assertThrows(ReglaDominioException::class.java) {
                    servicio.registrarCargo(folio, nuevoId(), concepto, Dinero(10.0))
                }
            }
            assertEquals(1, folio.listarCargos().size)
        }

        @Test
        fun `should reject a zero value charge`() {
            val folio = folio(cargo = 200.0)

            assertThrows(ReglaDominioException::class.java) {
                servicio.registrarCargo(folio, nuevoId(), ConceptoCargo.SERVICIO, Dinero(0))
            }
        }

        @Test
        fun `should reject a charge on a closed folio`() {
            val folio = folio(cargo = 200.0, pagado = 200.0)
            servicio.cerrar(folio)

            assertThrows(ReglaDominioException::class.java) {
                servicio.registrarCargo(folio, nuevoId(), ConceptoCargo.SERVICIO, Dinero(10.0))
            }
        }
    }

    @Nested
    inner class VerificarPagable {
        @Test
        fun `should reject a zero value payment`() {
            assertThrows(ReglaDominioException::class.java) {
                servicio.verificarPagable(folio(cargo = 200.0), Dinero(0))
            }
        }

        @Test
        fun `should reject a payment on a closed folio`() {
            val folio = folio(cargo = 200.0, pagado = 200.0)
            servicio.cerrar(folio)

            assertThrows(ReglaDominioException::class.java) {
                servicio.verificarPagable(folio, Dinero(10.0))
            }
        }
    }

    @Nested
    inner class Cerrar {
        @Test
        fun `should close a folio whose balance is zero`() {
            val folio = folio(cargo = 200.0, pagado = 200.0)
            servicio.cerrar(folio)
            assertEquals(EstadoFolio.CERRADO, folio.estadoActual())
        }

        @Test
        fun `should refuse to close a folio with an outstanding balance`() {
            val folio = folio(cargo = 200.0, pagado = 50.0)
            assertThrows(ReglaDominioException::class.java) { servicio.cerrar(folio) }
            assertEquals(EstadoFolio.ABIERTO, folio.estadoActual())
        }

        @Test
        fun `should refuse to close a folio that is already closed`() {
            val folio = folio(cargo = 200.0, pagado = 200.0)
            servicio.cerrar(folio)
            assertThrows(ReglaDominioException::class.java) { servicio.cerrar(folio) }
        }
    }
}