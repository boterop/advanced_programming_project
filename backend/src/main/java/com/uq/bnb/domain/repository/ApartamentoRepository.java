package com.uq.bnb.domain.repository;

import com.uq.bnb.domain.entity.Apartamento;
import java.util.List;
import java.util.Optional;

public interface ApartamentoRepository {
  void guardar(Apartamento apartamento);

  Optional<Apartamento> buscarPorId(String id);

  List<Apartamento> obtenerTodos();
}
