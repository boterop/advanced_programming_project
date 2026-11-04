package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.ModificarReservaCommand
import com.project.advanced.project.application.exception.ReservaNoEncontradaException
import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.CotizarEstanciaService
import com.project.advanced.project.domain.service.DisponibilidadModificacionService
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.TiempoPreparacion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ModificarReservaUseCase(
    private val reservaRepository: ReservaRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val disponibilidadModificacionService: DisponibilidadModificacionService,
    private val cotizarEstanciaService: CotizarEstanciaService,
) {
    fun ejecutar(comando: ModificarReservaCommand) {
        val codigo = CodigoReserva(comando.codigo)
        val reserva = reservaRepository.buscarPorCodigo(codigo) ?: throw ReservaNoEncontradaException(codigo)

        // Falla rapido: no se consulta disponibilidad de una reserva que ya no admite cambios.
        reserva.verificarModificable()

        val apartamento = resolverApartamento(comando, reserva)
        val estancia = resolverEstancia(comando, reserva)
        val ocupantes = resolverOcupantes(comando, reserva)
        val horaLlegada = comando.horaLlegada?.let { HoraLlegada(it) } ?: reserva.horaLlegada

        val cambiaOcupacion = apartamento != reserva.apartamento || estancia != reserva.estancia

        if (cambiaOcupacion) {
            disponibilidadModificacionService.verificarDisponibilidad(reserva.id, apartamento.id, estancia, TIEMPO_PREPARACION)
        }

        // RN-14: un cambio de fechas, apartamento u ocupantes recalcula con las tarifas vigentes.
        val recalcular = cambiaOcupacion || ocupantes != reserva.ocupantes
        val valor = if (recalcular) cotizarEstanciaService.cotizar(apartamento, estancia, ocupantes) else reserva.valor

        reserva.modificar(apartamento, estancia, ocupantes, horaLlegada, valor)

        reservaRepository.guardar(reserva)
    }

    private fun resolverApartamento(
        comando: ModificarReservaCommand,
        reserva: Reserva,
    ): Apartamento{
        val nuevoId = comando.apartamentoId?.let { ID(it) }

        if (nuevoId == null || nuevoId == reserva.apartamento.id) return reserva.apartamento

        return apartamentoRepository.buscar(nuevoId)
            ?: throw ReglaDominioException("No existe el apartamento con id ${nuevoId.valor}")
    }

    private fun resolverEstancia(
        comando: ModificarReservaCommand,
        reserva: Reserva,
    ): Estancia {
        val actual = reserva.estancia.periodo

        return Estancia(Periodo(comando.fechaEntrada ?: actual.inicio, comando.fechaSalida ?: actual.fin))
    }

    private fun resolverOcupantes(
        comando: ModificarReservaCommand,
        reserva: Reserva,
    ): List<Ocupante> =
        comando.otrosOcupantes
            ?.let { otros -> listOf(reserva.titular) + otros.map { it.aDominio() } }
            ?: reserva.ocupantes

    private companion object {
        // TODO: leer del parametro configurable del alojamiento; DisponibilidadApartamentoService aun no lo usa.
        val TIEMPO_PREPARACION = TiempoPreparacion(0)
    }
}