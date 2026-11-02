package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.CambiarEstadoApartamentoCommand
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.service.EstadoOperativoApartamentoService
import com.project.advanced.project.domain.valueobject.ID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CambiarEstadoApartamentoUseCase(
    private val apartamentoRepository: ApartamentoRepository,
    private val estadoOperativoApartamentoService: EstadoOperativoApartamentoService
) {
    fun ejecutar(comando: CambiarEstadoApartamentoCommand) {
        val apartamento =
            apartamentoRepository.buscar(ID(comando.apartamentoId))
                ?: throw ReglaDominioException("No existe el apartamento con id ${comando.apartamentoId}")
        estadoOperativoApartamentoService.cambiar(apartamento, comando.nuevoEstado)
        apartamentoRepository.guardar(apartamento)
    }
}