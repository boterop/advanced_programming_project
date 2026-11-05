package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.response.ApartamentoDisponibleResult
import com.project.advanced.project.application.dto.request.ConsultarDisponibilidadRequest
import com.project.advanced.project.application.dto.response.PaginaResponse
import com.project.advanced.project.application.dto.response.aDisponible
import com.project.advanced.project.domain.service.BusquedaDisponibilidadService
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.Pagina
import com.project.advanced.project.domain.valueobject.Periodo
import com.project.advanced.project.domain.valueobject.TiempoPreparacion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map

@Service
@Transactional(readOnly = true)
class ConsultarDisponibilidadUseCase(
    private val busquedaDisponibilidadService: BusquedaDisponibilidadService
) {
    fun ejecutar(consulta: ConsultarDisponibilidadRequest): PaginaResponse<ApartamentoDisponibleResult> {
        val estancia = Estancia(Periodo(consulta.fechaEntrada, consulta.fechaSalida))

        val disponibles =
            busquedaDisponibilidadService.buscarDisponibles(estancia, consulta.cantidadOcupantes, TIEMPO_PREPARACION)

        val pagina = Pagina.de(disponibles, consulta.pagina)

        return PaginaResponse(
            contenido = pagina.contenido.map { it.aDisponible() },
            pagina = pagina.numero,
            tamano = pagina.tamano,
            totalElementos = pagina.totalElementos,
            totalPaginas = pagina.totalPaginas,
        )
    }

    private companion object {
        // TODO: leer del parametro configurable del alojamiento; DisponibilidadApartamentoService aun no lo usa.
        val TIEMPO_PREPARACION = TiempoPreparacion(0)
    }
}