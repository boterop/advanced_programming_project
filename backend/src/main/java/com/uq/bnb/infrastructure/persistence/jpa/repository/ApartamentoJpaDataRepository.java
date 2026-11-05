package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.infrastructure.persistence.jpa.entity.ApartamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data: detalle interno de infraestructura, el dominio nunca lo ve. */
public interface ApartamentoJpaDataRepository extends JpaRepository<ApartamentoEntity, String> {
}
