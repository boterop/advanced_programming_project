package com.uq.bnb.application.usecase;
import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.service.CreacionReservaService;
import com.uq.bnb.domain.valueobject.SolicitudReserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Clock;
import java.time.LocalDateTime;
@Service
public class CrearReservaUseCase {
    private final AlojamientoRepository alojamientoRepository;
    private final ApartamentoRepository apartamentoRepository;
    private final ReservaRepository reservaRepository;
    private final CreacionReservaService creacionReservaService;
    private final Clock clock;
    private final IDProvider idProvider;
    public CrearReservaUseCase(AlojamientoRepository alojamientoRepository,
                               ApartamentoRepository apartamentoRepository,
                               ReservaRepository reservaRepository,
                               CreacionReservaService creacionReservaService,
                               Clock clock, IDProvider idProvider) {
        this.alojamientoRepository = alojamientoRepository;
        this.apartamentoRepository = apartamentoRepository;
        this.reservaRepository = reservaRepository;
        this.creacionReservaService = creacionReservaService;
        this.clock = clock;
        this.idProvider = idProvider;
    }
    @Transactional
    public Reserva ejecutar(String identificacionApartamento, SolicitudReserva solicitud) {
        Alojamiento alojamiento = alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
        Apartamento apartamento = apartamentoRepository.buscarPorId(identificacionApartamento)
                .orElseThrow(() -> new ApartamentoNoEncontradoException(identificacionApartamento));
        Reserva reserva = creacionReservaService.crear(alojamiento, apartamento, solicitud, LocalDateTime.now(clock), idProvider::obtener);
        reservaRepository.guardar(reserva);
        return reserva;
    }
}
