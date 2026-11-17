package com.uq.bnb.application.usecase;

import com.uq.bnb.application.exception.AlojamientoNoEncontradoException;
import com.uq.bnb.application.exception.ReservaNoEncontradaException;
import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.MedioPago;
import com.uq.bnb.domain.valueobject.RecargoLlegadaNocturna;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmarReservaUseCase {
  private final ReservaRepository reservaRepository;
  private final AlojamientoRepository alojamientoRepository;
  private final RecargoLlegadaNocturna recargoLlegadaNocturna;
  private final Clock clock;
  private final IDProvider idProvider;

  public ConfirmarReservaUseCase(
      ReservaRepository reservaRepository,
      AlojamientoRepository alojamientoRepository,
      RecargoLlegadaNocturna recargoLlegadaNocturna,
      Clock clock,
      IDProvider idProvider) {
    this.reservaRepository = reservaRepository;
    this.alojamientoRepository = alojamientoRepository;
    this.recargoLlegadaNocturna = recargoLlegadaNocturna;
    this.clock = clock;
    this.idProvider = idProvider;
  }

  @Transactional
  public Reserva ejecutar(
      CodigoReserva codigo, LocalTime horaEstimadaLlegada, MedioPago medioPago, String referencia) {
    Reserva reserva =
        reservaRepository
            .obtenerPorCodigo(codigo)
            .orElseThrow(() -> new ReservaNoEncontradaException(codigo));
    Alojamiento alojamiento =
        alojamientoRepository.obtener().orElseThrow(AlojamientoNoEncontradoException::new);
    if (horaEstimadaLlegada != null) {
      reserva.registrarHoraEstimadaLlegada(
          idProvider.obtener(), horaEstimadaLlegada, recargoLlegadaNocturna);
    }
    reserva.confirmarConAnticipo(
        idProvider.obtener(),
        alojamiento.parametros().politicaAnticipo(),
        alojamiento.parametros().plazoConfirmacion(),
        medioPago,
        referencia,
        LocalDateTime.now(clock));
    reservaRepository.guardar(reserva);
    return reserva;
  }
}
