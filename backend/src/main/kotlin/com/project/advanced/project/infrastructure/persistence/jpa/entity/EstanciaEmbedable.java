package com.project.advanced.project.infrastructure.persistence.jpa.entity;

import java.time.LocalDate;

public record EstanciaEmbedable(
    @Column()
    LocalDate fechaEntrada,
    @Column()
    LocalDate fechaSalida
) {
}
