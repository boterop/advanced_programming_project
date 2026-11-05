package com.uq.bnb.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;

@Embeddable
public record EstanciaEmbeddable(LocalDate entrada, LocalDate salida) {}
