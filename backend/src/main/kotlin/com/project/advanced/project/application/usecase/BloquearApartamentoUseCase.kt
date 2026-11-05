package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.BloquearApartamentoCommand
import com.project.advanced.project.application.dto.response.BloquearApartamentoResponse
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.service.BloqueoApartamentoService
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Periodo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class BloquearApartamentoUseCase(
    private val apartamentoRepository: ApartamentoRepository,
    private val bloqueoRepository: BloqueoRepository,
    private val bloqueoApartamentoService: BloqueoApartamentoService
) {
    fun ejecutar(comando: BloquearApartamentoCommand): BloquearApartamentoResponse {
        val apartamento =
            apartamentoRepository.buscar(ID(comando.apartamentoId))
                ?: throw ReglaDominioException("No existe el apartamento con id ${comando.apartamentoId}")

        val bloqueo =
            bloqueoApartamentoService.bloquear(
                ID(UUID.randomUUID().toString()),
                apartamento,
                Periodo(comando.fechaInicio, comando.fechaFin),
                comando.motivo,
                comando.observacion,
            )
        bloqueoRepository.guardar(bloqueo)
        return BloquearApartamentoResponse(bloqueo.id.valor)
    }
}