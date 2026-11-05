package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.CrearReservaRequest
import com.project.advanced.project.application.dto.response.CrearReservaResponse
import com.project.advanced.project.domain.entity.Cargo
import com.project.advanced.project.domain.entity.Folio
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.FolioRepository
import com.project.advanced.project.domain.repository.PoliticaCancelacionRepository
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.service.CotizarEstanciaService
import com.project.advanced.project.domain.service.DisponibilidadApartamentoService
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.ConceptoCargo
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.IdExterno
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.TiempoPreparacion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class CrearReservaUseCase(
    private val reservaRepository: ReservaRepository,
    private val apartamentoRepository: ApartamentoRepository,
    private val politicaCancelacionRepository: PoliticaCancelacionRepository,
    private val disponibilidadApartamentoService: DisponibilidadApartamentoService,
    private val cotizarEstanciaService: CotizarEstanciaService,
    private val folioRepository: FolioRepository,
)  {
    fun ejecutar(comando: CrearReservaRequest): CrearReservaResponse {
        val apartamento =
            apartamentoRepository.buscar(ID(comando.apartamentoId))
                ?: throw ReglaDominioException("No existe el apartamento con id ${comando.apartamentoId}")

        val politica =
            politicaCancelacionRepository.buscarVigente()
                ?: throw ReglaDominioException("No hay una politica de cancelacion vigente")

        val estancia = Estancia(Periodo(comando.fechaEntrada, comando.fechaSalida))
        val titular = comando.titular.aDominio()
        val ocupantes = listOf(titular) + comando.otrosOcupantes.map { it.aDominio() }

        // Lanza ReglaDominioException si hay solapamiento con reservas activas o bloqueos vigentes.
        disponibilidadApartamentoService.verificarDisponibilidad(apartamento.id, estancia, TIEMPO_PREPARACION)

        val id = ID(UUID.randomUUID().toString())

        val reserva =
            Reserva.crear(
                id,
                generarCodigoUnico(),
                apartamento,
                estancia,
                EstadoReserva.PENDIENTE,
                comando.canalOrigen,
                construirIdExterno(comando, id),
                titular,
                ocupantes,
                HoraLlegada(comando.horaLlegada),
                cotizarEstanciaService.cotizar(apartamento, estancia, ocupantes),
                politica,
                "",
            )

        reservaRepository.guardar(reserva)
        folioRepository.guardar(abrirFolio(reserva))

        return CrearReservaResponse(reserva.id.valor, reserva.codigo.valor, reserva.valor.cantidad())

    }

    private fun construirIdExterno(
        comando: CrearReservaRequest,
        id: ID,
    ): IdExterno {
        val valor =
            comando.idExterno
                ?: if (comando.canalOrigen == CanalOrigen.EXTERNO) {
                    throw ReglaDominioException("Las reservas externas requieren el identificador del canal")
                } else {
                    id.valor
                }

        return IdExterno(comando.canalOrigen, valor)
    }

    private fun generarCodigoUnico(): CodigoReserva =
        generateSequence { CodigoReserva("RES-" + UUID.randomUUID().toString().take(8).uppercase()) }
            .first { reservaRepository.buscarPorCodigo(it) == null }

    private companion object {
        // TODO: leer del parametro configurable del alojamiento; 
        //  DisponibilidadApartamentoService aun no lo usa.
        val TIEMPO_PREPARACION = TiempoPreparacion(0)
    }

    /** El folio se abre al crear la reserva con el cargo de alojamiento
     *  Comparte el id de la reserva. */
    private fun abrirFolio(reserva: Reserva): Folio =
        Folio.crear(
            reserva.id,
            mutableListOf(Cargo.crear(ID(UUID.randomUUID().toString()), ConceptoCargo.ALOJAMIENTO, reserva.valor)),
            mutableListOf(),
        )
}