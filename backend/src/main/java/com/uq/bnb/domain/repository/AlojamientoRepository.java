package com.uq.bnb.domain.repository;

import com.uq.bnb.domain.entity.Alojamiento;
import java.util.Optional;

public interface AlojamientoRepository {
  Optional<Alojamiento> obtener();

  void guardar(Alojamiento alojamiento);
}
