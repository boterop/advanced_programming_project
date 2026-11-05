package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.ApartamentoNoEncontradoException;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrarSalidaUseCase {
  private final ReservaRepository reservaRepository;
  private final ApartamentoRepository apartamentoRepository;

  public RegistrarSalidaUseCase(
      ReservaRepository reservaRepository, ApartamentoRepository apartamentoRepository) {
    this.reservaRepository = reservaRepository;
    this.apartamentoRepository = apartamentoRepository;
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
    reserva.realizarCheckOut();
    apartamento.registrarDesocupacion();
    reservaRepository.guardar(reserva);
    apartamentoRepository.guardar(apartamento);
    return reserva;
  }
}
