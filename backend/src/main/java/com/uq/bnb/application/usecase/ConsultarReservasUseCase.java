package com.uq.bnb.application.usecase;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
@Service
public class ConsultarReservasUseCase {
    private final ReservaRepository reservaRepository;
    public ConsultarReservasUseCase(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }
    @Transactional(readOnly = true)
    public Page<Reserva> ejecutar(EstadoReserva estado, String apartamentoId, LocalDate desde, LocalDate hasta,
                                  Pageable pageable) {
        return Paginacion.paginar(reservaRepository.buscar(estado, apartamentoId, desde, hasta), pageable);
    }
}
