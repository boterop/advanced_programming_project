package com.uq.bnb.infrastructure.persistence.jpa.repository;
import com.uq.bnb.infrastructure.persistence.jpa.entity.AlojamientoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface AlojamientoJpaDataRepository extends JpaRepository<AlojamientoEntity, UUID> {
}
