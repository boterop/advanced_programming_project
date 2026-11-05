package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.PoliticaCancelacionNoEncontradaException;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Caso de uso: cancelar una reserva. Busca la versión de la política CONGELADA en la reserva (RN-13), no la vigente
 * de hoy, y se la entrega a Reserva, que calcula la retención y hace la transición.
 */
@Service
public class CancelarReservaUseCase {

    private final ReservaRepository reservaRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final Clock clock;

    public CancelarReservaUseCase(ReservaRepository reservaRepository, AlojamientoRepository alojamientoRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.alojamientoRepository = alojamientoRepository;
        this.clock = clock;
    }

    @Transactional
    public Reserva ejecutar(CodigoReserva codigo, String motivo) {
        Reserva reserva = reservaRepository.obtenerPorCodigo(codigo).orElseThrow(() -> new ReservaNoEncontradaException(codigo));
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        PoliticaCancelacion politicaCongelada = alojamiento.politicaDeVersion(reserva.getVersionPolitica())
                .orElseThrow(() -> new PoliticaCancelacionNoEncontradaException(
                        "versión " + reserva.getVersionPolitica().numero() + " congelada en la reserva " + codigo.valor()));

        reserva.cancelar(politicaCongelada, motivo, LocalDateTime.now(clock));
        reservaRepository.guardar(reserva);
        return reserva;
    }
}
