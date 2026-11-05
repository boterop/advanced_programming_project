package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.TemporadaNoEncontradaException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Tarifa;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.valueobject.Dinero;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Caso de uso: definir la tarifa de un apartamento para una temporada (7.4). Orquesta: comprueba que existan el
 * apartamento y la temporada (agregados distintos, por eso se consultan por separado); la regla de que la nueva
 * tarifa rija después de la anterior vive en Apartamento.definirTarifa.
 */
@Service
public class DefinirTarifaUseCase {

    private final ApartamentoRepository apartamentoRepository;
    private final AlojamientoRepository alojamientoRepository;

    public DefinirTarifaUseCase(ApartamentoRepository apartamentoRepository, AlojamientoRepository alojamientoRepository) {
        this.apartamentoRepository = apartamentoRepository;
        this.alojamientoRepository = alojamientoRepository;
    }

    @Transactional
    public Tarifa ejecutar(String identificacionApartamento, UUID temporadaId, Dinero valorPorNoche, LocalDate vigenteDesde) {
        Apartamento apartamento = apartamentoRepository.buscarPorId(identificacionApartamento)
                .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento));
        alojamientoRepository.obtener()
                .orElseThrow(AlojamientoNoEncontradoException::new)
                .temporada(temporadaId)
                .orElseThrow(() -> new TemporadaNoEncontradaException(temporadaId));
        Tarifa nueva = apartamento.definirTarifa(temporadaId, valorPorNoche, vigenteDesde);
        apartamentoRepository.guardar(apartamento);
        return nueva;
    }
}
