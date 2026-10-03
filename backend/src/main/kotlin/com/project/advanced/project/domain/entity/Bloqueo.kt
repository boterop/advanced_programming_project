package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.ID
import com.project.advanced.project.domain.valueobject.MotivoBloqueo
import com.project.advanced.project.domain.valueobject.Periodo
import java.time.LocalDate

class Bloqueo private constructor(
    val id: ID,
    val apartamentoId: ID,
    val periodo: Periodo,
    val motivo: MotivoBloqueo,
    val observacion: String,
    var vigente: Boolean,
) {
    init {
        if (apartamentoId == null) throw ReglaDominioException("El apartamentoId no puede ser nulo")
        if (periodo == null) throw ReglaDominioException("El periodo no puede ser nulo")
        if (motivo == null) throw ReglaDominioException("El motivo no puede ser nulo")
        if (observacion == null) throw ReglaDominioException("La observacion no puede ser nula")

        vigente = true
    }

    companion object {
        fun crear(
            id: ID,
            apartamentoId: ID,
            periodo: Periodo,
            motivo: MotivoBloqueo,
            observacion: String,
        ): Bloqueo = Bloqueo(id, apartamentoId, periodo, motivo, observacion, true)
    }

    fun cubre(noche: LocalDate): Boolean = this.vigente && this.periodo.contiene(noche)

    fun levantar() {
        this.vigente = false
    }

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Bloqueo && id == other.id
}
