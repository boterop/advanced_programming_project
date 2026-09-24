package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.CanalOrigen
import com.project.advanced.project.domain.valueobject.CodigoReserva
import com.project.advanced.project.domain.valueobject.EstadoReserva
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.HoraLlegada
import com.project.advanced.project.domain.valueobject.IdExterno
import com.project.advanced.project.domain.valueobject.VersionPolitica
import java.time.LocalDate

class Reserva private constructor(
    val codigo: CodigoReserva,
    val apartamento: Apartamento,
    val estancia: Estancia,
    val estado: EstadoReserva,
    val canalOrigen: CanalOrigen,
    val idExterno: IdExterno,
    val titular: Ocupante,
    val ocupantes: List<Ocupante>,
    val horaLlegada: HoraLlegada,
    val valor: Double,
    val politica: VersionPolitica,
    val fechaCreacion: LocalDate,
    val motivoCancelacion: String,
) {
    init {
        if (codigo == null) throw ReglaDominioException("El codigo no puede ser nulo")
        if (apartamento == null) throw ReglaDominioException("El apartamento no puede ser nulo")
        if (estancia == null) throw ReglaDominioException("La estancia no puede ser nula")
        if (estado == null) throw ReglaDominioException("El estado no puede ser nulo")
        if (canalOrigen == null) throw ReglaDominioException("El canalOrigen no puede ser nulo")
        if (idExterno == null) throw ReglaDominioException("El idExterno no puede ser nulo")
        if (titular == null) throw ReglaDominioException("El titular no puede ser nulo")
        if (ocupantes == null) throw ReglaDominioException("Los ocupantes no pueden ser nulos")
        if (horaLlegada == null) throw ReglaDominioException("La horaLlegada no puede ser nula")
        if (valor < 0) throw ReglaDominioException("El valor no puede ser negativo")
        if (politica == null) throw ReglaDominioException("La politica no puede ser nula")
        if (fechaCreacion == null) throw ReglaDominioException("La fecha de creacion no puede ser nula")
        if (motivoCancelacion == null) throw ReglaDominioException("El motivo de cancelacion no puede ser nulo")

        val hoy = LocalDate.now()
        if (estancia.periodo.inicio.isBefore(hoy)) {
            throw ReglaDominioException("La fecha de inicio de la estancia no puede ser anterior a la fecha actual")
        }

        if (apartamento.admite(ocupantes.size)) {
            throw ReglaDominioException("El apartamento no admite el numero de ocupantes")
        }
    }

    companion object {
        fun crear(
            codigo: CodigoReserva,
            apartamento: Apartamento,
            estancia: Estancia,
            estado: EstadoReserva,
            canalOrigen: CanalOrigen,
            idExterno: IdExterno,
            titular: Ocupante,
            ocupantes: List<Ocupante>,
            horaLlegada: HoraLlegada,
            valor: Double,
            politica: VersionPolitica,
            fechaCreacion: LocalDate,
            motivoCancelacion: String,
        ): Reserva =
            Reserva(
                codigo,
                apartamento,
                estancia,
                estado,
                canalOrigen,
                idExterno,
                titular,
                ocupantes,
                horaLlegada,
                valor,
                politica,
                fechaCreacion,
                motivoCancelacion,
            )
    }
}
