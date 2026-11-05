package com.uq.bnb.application.dto.response;

import java.time.LocalDate;

public record OcupanteResponse(String documento, String nombre, LocalDate fechaNacimiento) {}
