package com.uq.bnb.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
public record DetalleNocheEmbeddable(
    LocalDate fecha,
    @Column(name = "nombre_temporada") String nombreTemporada,
    @Column(name = "tarifa_aplicada", precision = 19, scale = 0) BigDecimal tarifaAplicada,
    @Column(name = "ocupantes_facturables") int ocupantesFacturables) {}
