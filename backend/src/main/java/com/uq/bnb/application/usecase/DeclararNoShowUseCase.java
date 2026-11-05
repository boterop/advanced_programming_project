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
@Service
public class DeclararNoShowUseCase {
    private final ReservaRepository reservaRepository;
    private final AlojamientoRepository alojamientoRepository;
    private final Clock clock;
    public DeclararNoShowUseCase(ReservaRepository reservaRepository, AlojamientoRepository alojamientoRepository, Clock clock) {
        this.reservaRepository = reservaRepository;
        this.alojamientoRepository = alojamientoRepository;
        this.clock = clock;
    }
    @Transactional
    public Reserva ejecutar(CodigoReserva codigo) {
        Reserva reserva = reservaRepository.obtenerPorCodigo(codigo).orElseThrow(() -> new ReservaNoEncontradaException(codigo));
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        PoliticaCancelacion politicaCongelada = alojamiento.politicaDeVersion(reserva.versionPolitica())
                .orElseThrow(() -> new PoliticaCancelacionNoEncontradaException(
                        "versión " + reserva.versionPolitica().numero() + " congelada en la reserva " + codigo.valor()));
        reserva.declararNoShow(alojamiento.parametros().horaLimiteNoShow(), politicaCongelada, LocalDateTime.now(clock));
        reservaRepository.guardar(reserva);
        return reserva;
    }
}
