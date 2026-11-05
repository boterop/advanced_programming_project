package com.uq.bnb.infrastructure.persistence.jpa.entity;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.util.UUID;

@Embeddable
public record OcupanteEmbeddable(UUID id, String documento, String nombre, LocalDate fechaNacimiento) {
}
