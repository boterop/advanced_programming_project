package com.uq.bnb.application.dto.response;

import com.uq.bnb.domain.valueobject.CanalOrigen;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ReservaDetalleResponse(
    String codigo,
    EstadoReserva estado,
    CanalOrigen canalOrigen,
    String identificacionApartamento,
    LocalDate fechaEntrada,
    LocalDate fechaSalida,
    int noches,
    LocalTime horaEstimadaLlegada,
    boolean conMascota,
    OcupanteResponse titular,
    List<OcupanteResponse> ocupantes,
    int totalOcupantes,
    long valorTotal,
    long totalCargosFolio,
    String moneda,
    String versionPoliticaCancelacion,
    LocalDate fechaCreacion,
    String motivoCancelacion,
    Long retenidoPorCancelacion,
    Long devueltoPorCancelacion) {}
