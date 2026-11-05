package com.uq.bnb.application.dto.response;
import java.util.List;
public record AlojamientoResponse(
        String nombre,
        String ciudad,
        ParametrosResponse parametros,
        List<TemporadaResponse> temporadas,
        PoliticaCancelacionResponse politicaVigente
) {
}
