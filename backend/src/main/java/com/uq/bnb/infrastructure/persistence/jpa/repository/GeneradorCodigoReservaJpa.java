package com.uq.bnb.infrastructure.persistence.jpa.repository;

import com.uq.bnb.domain.repository.GeneradorCodigoReserva;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import java.time.Clock;
import java.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GeneradorCodigoReservaJpa implements GeneradorCodigoReserva {
  private final ReservaJpaDataRepository dataRepository;
  private final Clock clock;

  public GeneradorCodigoReservaJpa(ReservaJpaDataRepository dataRepository, Clock clock) {
    this.dataRepository = dataRepository;
    this.clock = clock;
  }

  @Override
  @Transactional(readOnly = true)
  public synchronized CodigoReserva generar() {
    int anio = LocalDate.now(clock).getYear();
    long consecutivo = dataRepository.countByCodigoStartingWith("RES-" + anio + "-") + 1;
    return CodigoReserva.de(anio, consecutivo);
  }
}
