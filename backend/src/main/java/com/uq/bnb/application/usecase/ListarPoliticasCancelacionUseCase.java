package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso: consultar todas las versiones de la política de cancelación, de la primera a la más reciente (7.4). */
@Service
public class ListarPoliticasCancelacionUseCase {

    private final AlojamientoRepository alojamientoRepository;

    public ListarPoliticasCancelacionUseCase(AlojamientoRepository alojamientoRepository) {
        this.alojamientoRepository = alojamientoRepository;
    }

    @Transactional(readOnly = true)
    public Page<PoliticaCancelacion> ejecutar(Pageable pageable) {
        return Paginacion.paginar(
                alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new).historialPoliticas(),
                pageable);
    }
}
