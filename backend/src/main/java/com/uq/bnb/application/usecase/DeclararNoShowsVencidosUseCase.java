package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.PoliticaCancelacionNoEncontradaException;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.HoraLimiteNoShow;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Caso de uso: pasar automáticamente a NO_SHOW las reservas CONFIRMADA cuyo titular no llegó antes de la hora límite del
 * día de entrada (RN-12). Lo dispara un proceso programado; no tiene endpoint.
 */
@Service
public class DeclararNoShowsVencidosUseCase {

    private final ReservaRepository reservaRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final Clock clock;

    public DeclararNoShowsVencidosUseCase(ReservaRepository reservaRepository, AlojamientoRepository alojamientoRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.alojamientoRepository = alojamientoRepository;
        this.clock = clock;
    }

    /** @return cuántas reservas se declararon no-show */
    @Transactional
    public int ejecutar() {
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        HoraLimiteNoShow horaLimite = alojamiento.parametros().horaLimiteNoShow();
        LocalDateTime ahora = LocalDateTime.now(clock);

        List<Reserva> ausentes = reservaRepository.buscarPorEstado(EstadoReserva.CONFIRMADA).stream()
                .filter(r -> r.haPasadoHoraLimiteNoShow(horaLimite, ahora))
                .toList();
        for (Reserva reserva : ausentes) {
            PoliticaCancelacion politicaCongelada = alojamiento.politicaDeVersion(reserva.getVersionPolitica())
                    .orElseThrow(() -> new PoliticaCancelacionNoEncontradaException(
                            "versión " + reserva.getVersionPolitica().numero() + " congelada en la reserva " + reserva.getCodigo().valor()));
            reserva.declararNoShow(horaLimite, politicaCongelada, ahora);
            reservaRepository.guardar(reserva);
        }
        return ausentes.size();
    }
}
