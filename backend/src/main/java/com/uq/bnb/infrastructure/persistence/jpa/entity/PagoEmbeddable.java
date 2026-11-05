package com.uq.bnb.infrastructure.persistence.jpa.entity;

import com.uq.bnb.domain.valueobject.MedioPago;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
public record PagoEmbeddable(
    UUID id,
    @Enumerated(EnumType.STRING) MedioPago medio,
    LocalDate fecha,
    @Column(precision = 19, scale = 0) BigDecimal valor) {}
