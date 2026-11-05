package com.uq.bnb.infrastructure.persistence.jpa.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
@Embeddable
public record TramoEmbeddable(
        int antelacionMinimaHoras,
        @Column(precision = 5, scale = 4) BigDecimal porcentajeRetencion) {
}
