package com.uq.bnb.domain.service;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.repository.ReservaRepository;
import java.util.Objects;

public class GestionBloqueoService {
  private final ReservaRepository reservaRepository;

  public GestionBloqueoService(ReservaRepository reservaRepository) {
    this.reservaRepository =
        Objects.requireNonNull(reservaRepository, "El repositorio de reservas no puede ser nulo");
  }

  public void registrarBloqueo(Apartamento apartamento, Bloqueo bloqueo) {
    Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
    Objects.requireNonNull(bloqueo, "El bloqueo no puede ser nulo");
    boolean hayReservaConflicto =
        !reservaRepository
            .buscarActivasPorApartamento(apartamento.identificacion(), bloqueo.rango())
            .isEmpty();
    if (hayReservaConflicto) {
      throw new ReglaDominioException(
          "RN-07 / 7.3: no se puede bloquear el apartamento "
              + apartamento.identificacion()
              + " del "
              + bloqueo.rango().entrada()
              + " al "
              + bloqueo.rango().salida()
              + ": existen reservas activas en esas noches");
    }
    apartamento.agregarBloqueo(bloqueo);
  }
}
