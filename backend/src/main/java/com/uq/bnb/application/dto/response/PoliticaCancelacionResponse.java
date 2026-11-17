package com.uq.bnb.application.dto.response;

import java.time.LocalDate;
import java.util.List;

public record PoliticaCancelacionResponse(
    int version, LocalDate vigenteDesde, List<TramoResponse> tramos) {}
