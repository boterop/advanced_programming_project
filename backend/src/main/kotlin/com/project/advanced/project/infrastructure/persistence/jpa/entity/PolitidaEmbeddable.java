package com.project.advanced.project.infrastructure.persistence.jpa.entity;

import java.time.LocalDate;

public record PolitidaEmbeddable(
        @Column(name = "version", nullable = false)
        int numero,
        @Column(name = "fecha_vigencia", nullable = false)
        LocalDate vigenteDesde
) {
}
