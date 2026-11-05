package com.uq.bnb.domain.service;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import java.util.List;
import java.util.Objects;

public class RegistroApartamentoService {
  private final ApartamentoRepository apartamentoRepository;
  private final ReservaRepository reservaRepository;

  public RegistroApartamentoService(
      ApartamentoRepository apartamentoRepository, ReservaRepository reservaRepository) {
    this.apartamentoRepository = Objects.requireNonNull(apartamentoRepository);
    this.reservaRepository = Objects.requireNonNull(reservaRepository);
  }

  public void registrar(Apartamento apartamento) {
    Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
    if (apartamentoRepository.buscarPorId(apartamento.identificacion()).isPresent()) {
      throw new ReglaDominioException(
          "Ya existe un apartamento con la identificación "
              + apartamento.identificacion()
              + " (L-04)");
    }
    apartamentoRepository.guardar(apartamento);
  }

  public void retirar(Apartamento apartamento) {
    Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
    List<Reserva> activas =
        reservaRepository.buscarActivasPorApartamento(apartamento.identificacion());
    if (!activas.isEmpty()) {
      throw new ReglaDominioException(
          "7.3: no se puede retirar el apartamento "
              + apartamento.identificacion()
              + " porque tiene "
              + activas.size()
              + " reserva(s) activa(s) o futura(s)");
    }
    apartamento.desactivar();
  }

  public void cambiarCapacidad(Apartamento apartamento, int nuevaCapacidad) {
    Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
    reservaRepository.buscarActivasPorApartamento(apartamento.identificacion()).stream()
        .filter(reserva -> reserva.cuposNecesarios() > nuevaCapacidad)
        .findFirst()
        .ifPresent(
            reserva -> {
              throw new ReglaDominioException(
                  "RN-02: la reserva "
                      + reserva.codigo().valor()
                      + " ocupa "
                      + reserva.cuposNecesarios()
                      + " cupos; la capacidad del apartamento "
                      + apartamento.identificacion()
                      + " no puede bajar a "
                      + nuevaCapacidad);
            });
    apartamento.cambiarCapacidad(nuevaCapacidad);
  }
}
