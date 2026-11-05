package com.uq.bnb.infrastructure.persistence.inmemory;

import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.Estancia;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@Profile("inmemory")
public class ReservaRepositoryInMemory implements ReservaRepository {

    private final Map<UUID, Reserva> tablaReservas = new HashMap<>();

    @Override
    public List<Reserva> buscarActivasPorApartamento(String apartamentoId, Estancia estancia) {
        return tablaReservas.values().stream()
                .filter(reserva -> reserva.getApartamentoId().equals(apartamentoId))
                .filter(reserva -> reserva.getEstado() != null && reserva.getEstado().esActiva())
                .filter(reserva -> reserva.getEstancia().seSolapaCon(estancia))
                .toList();
    }

    @Override
    public List<Reserva> buscarActivasPorApartamento(String apartamentoId) {
        return tablaReservas.values().stream()
                .filter(reserva -> reserva.getApartamentoId().equals(apartamentoId))
                .filter(reserva -> reserva.getEstado() != null && reserva.getEstado().esActiva())
                .toList();
    }

    @Override
    public Optional<Reserva> obtenerPorCodigo(CodigoReserva codigo) {
        return tablaReservas.values().stream().filter(r -> r.getCodigo().equals(codigo)).findFirst();
    }

    @Override
    public List<Reserva> buscarPorEstado(EstadoReserva estado) {
        return tablaReservas.values().stream()
                .filter(reserva -> reserva.getEstado() == estado)
                .toList();
    }

    @Override
    public List<Reserva> buscar(EstadoReserva estado, String apartamentoId, LocalDate desde, LocalDate hasta) {
        return tablaReservas.values().stream()
                .filter(reserva -> estado == null || reserva.getEstado() == estado)
                .filter(reserva -> apartamentoId == null || reserva.getApartamentoId().equals(apartamentoId))
                .filter(reserva -> desde == null || reserva.getEstancia().salida().isAfter(desde))
                .filter(reserva -> hasta == null || reserva.getEstancia().entrada().isBefore(hasta))
                .sorted(Comparator.comparing(Reserva::getFechaCreacion).reversed())
                .toList();
    }

    @Override
    public void guardar(Reserva reserva) {
        tablaReservas.put(reserva.getId(), reserva);
    }
}
