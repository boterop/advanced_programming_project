package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.TemporadaNoEncontradaException;
import com.uq.bnb.domain.entity.Temporada;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** Caso de uso: consultar una temporada por su identificador (7.4). */
@Service
public class ConsultarTemporadaUseCase {

    private final AlojamientoRepository alojamientoRepository;

    public ConsultarTemporadaUseCase(AlojamientoRepository alojamientoRepository) {
        this.alojamientoRepository = alojamientoRepository;
    }

    @Transactional(readOnly = true)
    public Temporada ejecutar(UUID id) {
        return alojamientoRepository.obtener()
                .orElseThrow(AlojamientoNoEncontradoException::new)
                .temporada(id)
                .orElseThrow(() -> new TemporadaNoEncontradaException(id));
    }
}
