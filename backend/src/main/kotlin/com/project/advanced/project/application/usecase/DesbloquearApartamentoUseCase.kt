package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.DesbloquearApartamentoRequest
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.BloqueoRepository
import com.project.advanced.project.domain.service.BloqueoApartamentoService
import com.project.advanced.project.domain.valueobject.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DesbloquearApartamentoUseCase(
    private val bloqueoRepository: BloqueoRepository,
    private val bloqueoApartamentoService: BloqueoApartamentoService
) {
    fun ejecutar(comando: DesbloquearApartamentoRequest) {
        val bloqueo =
            bloqueoRepository.buscar(ID(comando.bloqueoId))
                ?: throw ReglaDominioException("No existe el bloqueo con id ${comando.bloqueoId}")
        bloqueoApartamentoService.desbloquear(bloqueo)
        bloqueoRepository.guardar(bloqueo)
    }
}