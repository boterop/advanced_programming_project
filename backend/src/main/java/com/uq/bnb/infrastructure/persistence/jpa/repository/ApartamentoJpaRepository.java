package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.infrastructure.persistence.jpa.mapper.ApartamentoPersistenceMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class ApartamentoJpaRepository implements ApartamentoRepository {
  private final ApartamentoJpaDataRepository dataRepository;
  private final ApartamentoPersistenceMapper mapper;

  public ApartamentoJpaRepository(
      ApartamentoJpaDataRepository dataRepository, ApartamentoPersistenceMapper mapper) {
    this.dataRepository = dataRepository;
    this.mapper = mapper;
  }

  @Override
  public void guardar(Apartamento apartamento) {
    dataRepository.save(mapper.toEntity(apartamento));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Apartamento> buscarPorId(String id) {
    try {
      return dataRepository.findById(UUID.fromString(id)).map(mapper::toDomain);
    } catch (IllegalArgumentException ex) {
      return Optional.empty();
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<Apartamento> obtenerTodos() {
    return dataRepository.findAll().stream().map(mapper::toDomain).toList();
  }
}
