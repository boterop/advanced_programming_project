package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.infrastructure.persistence.jpa.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioJpaDataRepository extends JpaRepository<UsuarioEntity, UUID> {
    Optional<UsuarioEntity> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
    boolean existsByDocumento(String documento);
}
