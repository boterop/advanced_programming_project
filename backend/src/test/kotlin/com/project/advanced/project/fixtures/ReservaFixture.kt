package com.project.advanced.project.fixtures

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.entity.PoliticaCancelacion
import com.project.advanced.project.domain.entity.Reserva
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.IdExterno
import com.project.advanced.project.domain.valueobject.Periodo
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

object ReservaFixture {
    val periodo = Periodo(LocalDate.now(), LocalDate.now().plusDays(3))

    operator fun invoke(
        codigo: CodigoReserva = CodigoReserva(UUID.randomUUID().toString()),
        apartamento: Apartamento = ApartamentoFixture(),
        estancia: Estancia = Estancia(periodo),
        estado: EstadoReserva = EstadoReserva.CONFIRMADA,
        canalOrigen: CanalOrigen = CanalOrigen.DIRECTO,
        idExterno: IdExterno = IdExterno(CanalOrigen.DIRECTO, "ext-1"),
        titular: Ocupante = OcupanteFixture(),
        ocupantes: List<Ocupante> = listOf(titular),
        horaLlegada: HoraLlegada = HoraLlegada(LocalTime.of(15, 0)),
        valor: Double = 200.0,
        politica: PoliticaCancelacion = PoliticaCancelacionFixture(),
        motivoCancelacion: String = "Uso interno",
    ) = Reserva.crear(
        codigo,
        apartamento,
        estancia,
        estado,
        canalOrigen,
        idExterno,
        titular,
        ocupantes,
        horaLlegada,
        Dinero(valor),
        politica,
        motivoCancelacion,
    )
}
