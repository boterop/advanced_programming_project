package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.infrastructure.persistence.jpa.entity.AlojamientoEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlojamientoJpaDataRepository extends JpaRepository<AlojamientoEntity, UUID> {}
