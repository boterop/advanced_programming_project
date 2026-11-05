package com.uq.bnb.infrastructure.persistence.jpa.entity;

import com.uq.bnb.domain.valueobject.TipoCargo;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import java.math.BigDecimal;
import java.util.UUID;

@Embeddable
public record CargoEmbeddable(
    UUID id,
    @Enumerated(EnumType.STRING) TipoCargo tipo,
    @Column(precision = 19, scale = 0) BigDecimal valor) {}
