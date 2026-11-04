package com.project.advanced.project.infrastructure.persistence.jpa.entity;

import java.time.LocalDate;

public record OcupanteEmbeddable(
        @Column(name = "documento", nullable = false, length = 15)
        String documento;
        @Column(name = "", nullable = false, length = 15)
        String nombreCompleto,
        LocalDate fechaNacimiento
) {
}
