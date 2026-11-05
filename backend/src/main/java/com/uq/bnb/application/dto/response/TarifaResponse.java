package com.uq.bnb.application.dto.response;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
public record TarifaResponse(UUID id, UUID temporadaId, BigDecimal valorPorNoche, LocalDate vigenteDesde) {
}
