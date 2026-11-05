package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Folio;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.MedioPago;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;

/** Caso de uso: registrar un pago contra el folio de una reserva (RN-15). Las reglas viven en Reserva y Folio. */
@Service
public class RegistrarPagoUseCase {

    private final ReservaRepository reservaRepository;
    private final Clock clock;

    public RegistrarPagoUseCase(ReservaRepository reservaRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.clock = clock;
    }

    @Transactional
    public Folio ejecutar(CodigoReserva codigo, Dinero valor, MedioPago medioPago, String referencia) {
        Reserva reserva = reservaRepository.obtenerPorCodigo(codigo).orElseThrow(() -> new ReservaNoEncontradaException(codigo));
        reserva.registrarPago(valor, medioPago, referencia, LocalDate.now(clock));
        reservaRepository.guardar(reserva);
        return reserva.getFolio();
    }
}
