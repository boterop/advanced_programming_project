package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.PlazoConfirmacion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class CancelarReservasVencidasUseCase {
    private final ReservaRepository reservaRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final Clock clock;
    public CancelarReservasVencidasUseCase(ReservaRepository reservaRepository, AlojamientoRepository alojamientoRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.alojamientoRepository = alojamientoRepository;
        this.clock = clock;
    }
    @Transactional
    public int ejecutar() {
        PlazoConfirmacion plazo = alojamientoRepository.obtener()
                .orElseThrow(AlojamientoNoEncontradoException::new)
                .parametros().plazoConfirmacion();
        LocalDateTime ahora = LocalDateTime.now(clock);
        List<Reserva> vencidas = reservaRepository.buscarPorEstado(EstadoReserva.PENDIENTE).stream()
                .filter(r -> r.haVencidoPlazoConfirmacion(plazo, ahora))
                .toList();
        vencidas.forEach(r -> {
            r.cancelarPorVencimiento(plazo, ahora);
            reservaRepository.guardar(r);
        });
        return vencidas.size();
    }
}
