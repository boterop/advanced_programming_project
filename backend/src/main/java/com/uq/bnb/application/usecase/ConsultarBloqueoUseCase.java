package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.BloqueoNoEncontradoException;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/** Caso de uso: consultar un bloqueo de un apartamento (RN-07). */
@Service
public class ConsultarBloqueoUseCase {

    private final ApartamentoRepository apartamentoRepository;

    public ConsultarBloqueoUseCase(ApartamentoRepository apartamentoRepository) {
        this.apartamentoRepository = apartamentoRepository;
    }

    @Transactional(readOnly = true)
    public Bloqueo ejecutar(String identificacionApartamento, UUID idBloqueo) {
        return apartamentoRepository.buscarPorId(identificacionApartamento)
                .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento))
                .bloqueo(idBloqueo)
                .orElseThrow(() -> new BloqueoNoEncontradoException(identificacionApartamento, idBloqueo));
    }
}
