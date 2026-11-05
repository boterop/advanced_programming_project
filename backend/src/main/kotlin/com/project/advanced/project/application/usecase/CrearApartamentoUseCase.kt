package com.project.advanced.project.application.usecase

import com.project.advanced.project.application.dto.request.CrearApartamentoCommand
import com.project.advanced.project.application.dto.response.CrearApartamentoResponse
import com.project.advanced.project.domain.repository.ApartamentoRepository
import com.project.advanced.project.domain.service.CatalogoApartamentoService
import com.project.advanced.project.domain.valueobject.Capacidad
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.Nombre
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class CrearApartamentoUseCase(
    private val apartamentoRepository: ApartamentoRepository,
    private val catalogoApartamentoService: CatalogoApartamentoService
) {
    fun ejecutar(comando: CrearApartamentoCommand): CrearApartamentoResponse {
        val apartamento =
            catalogoApartamentoService.crear(
                ID(UUID.randomUUID().toString()),
                Nombre(comando.nombre),
                comando.dormitorios.map { it.aDominio() },
                Capacidad(comando.capacidad),
                comando.tarifas.map { it.aDominio() },
            )
        apartamentoRepository.guardar(apartamento)
        return CrearApartamentoResponse(apartamento.id.valor)
    }
}