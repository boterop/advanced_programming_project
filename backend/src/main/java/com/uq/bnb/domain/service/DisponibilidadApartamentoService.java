package com.uq.bnb.domain.service;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class DisponibilidadApartamentoService {
  private final ApartamentoRepository apartamentoRepository;
  private final ReservaRepository reservaRepository;

  public DisponibilidadApartamentoService(
      ApartamentoRepository apartamentoRepository, ReservaRepository reservaRepository) {
    this.apartamentoRepository =
        Objects.requireNonNull(
            apartamentoRepository, "El repositorio de apartamentos no puede ser nulo");
    this.reservaRepository =
        Objects.requireNonNull(reservaRepository, "El repositorio de reservas no puede ser nulo");
  }

  public boolean estaDisponible(
      Apartamento apartamento,
      Estancia estancia,
      int totalOcupantes,
      boolean conMascota,
      ParametrosAlojamiento parametros) {
    Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
    Objects.requireNonNull(estancia, "La estancia no puede ser nula");
    Objects.requireNonNull(parametros, "Los parámetros del alojamiento no pueden ser nulos");
    boolean permiteRecambioMismoDia =
        parametros.horario().permiteRecambioMismoDia(parametros.tiempoPreparacion());
    Estancia consulta =
        permiteRecambioMismoDia
            ? estancia
            : new Estancia(estancia.entrada().minusDays(1), estancia.salida().plusDays(1));
    List<Estancia> estanciasReservadas =
        reservaRepository
            .buscarActivasPorApartamento(apartamento.identificacion(), consulta)
            .stream()
            .map(Reserva::estancia)
            .toList();
    return apartamento.estaDisponible(
        estancia, totalOcupantes, conMascota, estanciasReservadas, permiteRecambioMismoDia);
  }

  public List<Apartamento> buscarDisponibles(
      Estancia estancia, int totalOcupantes, boolean conMascota, ParametrosAlojamiento parametros) {
    return apartamentoRepository.obtenerTodos().stream()
        .filter(
            apartamento ->
                estaDisponible(apartamento, estancia, totalOcupantes, conMascota, parametros))
        .sorted(Comparator.comparing(Apartamento::identificacion))
        .toList();
  }
}
