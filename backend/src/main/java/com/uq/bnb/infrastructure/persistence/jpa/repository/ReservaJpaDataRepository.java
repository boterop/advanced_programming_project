package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.infrastructure.persistence.jpa.entity.ReservaEntity;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservaJpaDataRepository
    extends JpaRepository<ReservaEntity, UUID>, JpaSpecificationExecutor<ReservaEntity> {
  Optional<ReservaEntity> findByCodigo(String codigo);

  List<ReservaEntity> findByEstado(EstadoReserva estado);

  List<ReservaEntity> findByApartamentoIdAndEstadoIn(
      String apartamentoId, Collection<EstadoReserva> estados);

  long countByCodigoStartingWith(String prefijo);

  @Query(
      """
            select r from ReservaEntity r
            where r.apartamentoId = :apartamentoId
              and r.estado in :estados
              and r.estancia.entrada < :salida
              and r.estancia.salida > :entrada
            """)
  List<ReservaEntity> buscarSolapadas(
      @Param("apartamentoId") String apartamentoId,
      @Param("estados") Collection<EstadoReserva> estados,
      @Param("entrada") LocalDate entrada,
      @Param("salida") LocalDate salida);
}
