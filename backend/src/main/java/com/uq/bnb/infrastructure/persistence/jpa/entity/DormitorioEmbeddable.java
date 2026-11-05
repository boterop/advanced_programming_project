package com.uq.bnb.infrastructure.persistence.jpa.entity;
import jakarta.persistence.Embeddable;
@Embeddable
public record DormitorioEmbeddable(String descripcion, String tipoCama) {
}
