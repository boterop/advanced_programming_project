package com.uq.bnb.application.dto.response;

import com.uq.bnb.domain.valueobject.EstadoOperativo;
import java.util.List;

public record ApartamentoResponse(
    String identificacion,
    String nombre,
    int capacidad,
    List<DormitorioResponse> dormitorios,
    boolean permiteMascotas,
    EstadoOperativo estadoOperativo,
    boolean activo) {}
