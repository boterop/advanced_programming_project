package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.infrastructure.persistence.jpa.mapper.AlojamientoPersistenceMapper;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class AlojamientoJpaRepository implements AlojamientoRepository {
  private final AlojamientoJpaDataRepository dataRepository;
  private final AlojamientoPersistenceMapper mapper;
  private final ParametrosAlojamiento parametros;

  public AlojamientoJpaRepository(
      AlojamientoJpaDataRepository dataRepository,
      AlojamientoPersistenceMapper mapper,
      ParametrosAlojamiento parametros) {
    this.dataRepository = dataRepository;
    this.mapper = mapper;
    this.parametros = parametros;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Alojamiento> obtener() {
    return dataRepository.findAll().stream()
        .findFirst()
        .map(entity -> mapper.toDomain(entity, parametros));
  }

  @Override
  public void guardar(Alojamiento alojamiento) {
    dataRepository.save(mapper.toEntity(alojamiento));
  }
}
