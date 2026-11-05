package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Folio;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Caso de uso: consultar el folio (cargos, pagos y saldo) de una reserva. */
@Service
public class ConsultarFolioUseCase {

    private final ReservaRepository reservaRepository;

    public ConsultarFolioUseCase(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    @Transactional(readOnly = true)
    public Folio ejecutar(CodigoReserva codigo) {
        return reservaRepository.obtenerPorCodigo(codigo)
                .orElseThrow(() -> new ReservaNoEncontradaException(codigo))
                .getFolio();
    }
}
