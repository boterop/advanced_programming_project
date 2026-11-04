package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.ListarReservasQuery
import com.project.advanced.project.application.dto.PaginaResult
import com.project.advanced.project.application.dto.ReservaResumenResult
import com.project.advanced.project.application.dto.aResumen
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ReservaRepository
import com.project.advanced.project.domain.valueobject.FiltroReservas
import com.project.advanced.project.domain.valueobject.Pagina
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ListarReservasUseCase(
    private val reservaRepository: ReservaRepository
) {
    fun ejecutar(consulta: ListarReservasQuery): PaginaResult<ReservaResumenResult> {
        if (consulta.pagina < 1) throw ReglaDominioException("El numero de pagina debe ser mayor o igual a 1")

        val filtro = FiltroReservas(consulta.estado, consulta.fechaDesde, consulta.fechaHasta, consulta.canalOrigen)
        val pagina = reservaRepository.buscar(filtro, consulta.pagina, Pagina.TAMANO)

        return PaginaResult(
            contenido = pagina.contenido.map { it.aResumen() },
            pagina = pagina.numero,
            tamano = pagina.tamano,
            totalElementos = pagina.totalElementos,
            totalPaginas = pagina.totalPaginas,
        )
    }
}