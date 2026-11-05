package com.uq.bnb.application.dto.response;

import java.math.BigDecimal;

public record TramoResponse(int antelacionMinimaHoras, BigDecimal porcentajeRetencion, BigDecimal porcentajeDevolucion) {
}
