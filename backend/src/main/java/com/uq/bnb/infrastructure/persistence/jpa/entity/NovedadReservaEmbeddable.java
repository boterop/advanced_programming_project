package com.uq.bnb.infrastructure.persistence.jpa.entity;
import com.uq.bnb.domain.valueobject.Gravedad;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.time.LocalDate;
import java.util.UUID;
@Embeddable
public record NovedadReservaEmbeddable(
        UUID id,
        LocalDate fecha,
        String autor,
        @Column(length = 1000) String descripcion,
        @Enumerated(EnumType.STRING) Gravedad gravedad) {
}
