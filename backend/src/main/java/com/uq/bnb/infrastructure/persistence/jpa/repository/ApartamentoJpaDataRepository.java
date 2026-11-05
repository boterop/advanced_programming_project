package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.infrastructure.persistence.jpa.entity.ApartamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApartamentoJpaDataRepository extends JpaRepository<ApartamentoEntity, String> {}
