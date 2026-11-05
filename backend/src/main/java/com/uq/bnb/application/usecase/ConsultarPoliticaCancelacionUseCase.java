package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.PoliticaCancelacionNoEncontradaException;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso: consultar una versión de la política de cancelación por su número (7.4). */
@Service
public class ConsultarPoliticaCancelacionUseCase {

    private final AlojamientoRepository alojamientoRepository;

    public ConsultarPoliticaCancelacionUseCase(AlojamientoRepository alojamientoRepository) {
        this.alojamientoRepository = alojamientoRepository;
    }

    @Transactional(readOnly = true)
    public PoliticaCancelacion ejecutar(int version) {
        return alojamientoRepository.obtener()
                .orElseThrow(AlojamientoNoEncontradoException::new)
                .politicaPorNumero(version)
                .orElseThrow(() -> new PoliticaCancelacionNoEncontradaException("versión " + version));
    }
}
