package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.application.port.IDProvider;
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
@Service
public class RegistrarPagoUseCase {
    private final ReservaRepository reservaRepository;
    private final Clock clock;
    private final IDProvider idProvider;
    public RegistrarPagoUseCase(ReservaRepository reservaRepository, Clock clock, IDProvider idProvider) {
        this.reservaRepository = reservaRepository;
        this.clock = clock;
        this.idProvider = idProvider;
    }
    @Transactional
    public Folio ejecutar(CodigoReserva codigo, Dinero valor, MedioPago medioPago, String referencia) {
        Reserva reserva = reservaRepository.obtenerPorCodigo(codigo).orElseThrow(() -> new ReservaNoEncontradaException(codigo));
        reserva.registrarPago(idProvider.obtener(), valor, medioPago, referencia, LocalDate.now(clock));
        reservaRepository.guardar(reserva);
        return reserva.folio();
    }
}
