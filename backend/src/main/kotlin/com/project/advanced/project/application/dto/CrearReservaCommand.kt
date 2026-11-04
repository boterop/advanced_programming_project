package com.project.advanced.project.application.dto

import com.project.advanced.project.domain.valueobject.CanalOrigen
import java.time.LocalDate
import java.time.LocalTime

data class CrearReservaCommand(
    val apartamentoId: String,
    val fechaEntrada: LocalDate,
    val fechaSalida: LocalDate,
    val horaLlegada: LocalTime,
    val canalOrigen: CanalOrigen,
    val titular: OcupanteCommand,
    val otrosOcupantes: List<OcupanteCommand> = emptyList(),
    val idExterno: String? = null,) /** Obligatorio para el canal EXTERNO;
                                        en PORTAL y DIRECTO se usa el id de la reserva. */
