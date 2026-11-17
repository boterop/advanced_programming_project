package com.uq.bnb.infrastructure.persistence.inmemory;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("inmemory")
public class ApartamentoRepositoryInMemory implements ApartamentoRepository {
  private final Map<String, Apartamento> repositorio = new HashMap<>();

  @Override
  public void guardar(Apartamento apartamento) {
    repositorio.put(apartamento.id().valor(), apartamento);
  }

  @Override
  public Optional<Apartamento> buscarPorId(String id) {
    return Optional.ofNullable(repositorio.get(id));
  }

  @Override
  public List<Apartamento> obtenerTodos() {
    return repositorio.values().stream().toList();
  }
}
