package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.service.EntregaApartamentoService;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarLlegadaUseCase {
  private final ReservaRepository reservaRepository;
  private final ApartamentoRepository apartamentoRepository;
  private final EntregaApartamentoService entregaApartamentoService;
  private final Clock clock;

  public RegistrarLlegadaUseCase(
      ReservaRepository reservaRepository,
      ApartamentoRepository apartamentoRepository,
      EntregaApartamentoService entregaApartamentoService,
      Clock clock) {
    this.reservaRepository = reservaRepository;
    this.apartamentoRepository = apartamentoRepository;
    this.entregaApartamentoService = entregaApartamentoService;
    this.clock = clock;
  }

  @Transactional
  public Reserva ejecutar(CodigoReserva codigo) {
    Reserva reserva =
        reservaRepository
            .obtenerPorCodigo(codigo)
            .orElseThrow(() -> new ReservaNoEncontradaException(codigo));
    Apartamento apartamento =
        apartamentoRepository
            .buscarPorId(reserva.apartamentoId())
            .orElseThrow(() -> new ApartamentoNoEncontradoException(reserva.apartamentoId()));
    entregaApartamentoService.autorizarEntrega(reserva, apartamento, LocalDate.now(clock));
    reserva.realizarCheckIn();
    apartamento.registrarOcupacion();
    reservaRepository.guardar(reserva);
    apartamentoRepository.guardar(apartamento);
    return reserva;
  }
}
