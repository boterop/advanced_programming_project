package com.project.advanced.project.domain.service

import com.project.advanced.project.domain.entity.Apartamento
import com.project.advanced.project.domain.entity.Ocupante
import com.project.advanced.project.domain.exception.ReglaDominioException
import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.Estancia
import com.project.advanced.project.domain.valueobject.Tarifa
import com.project.advanced.project.domain.valueobject.Temporada
import org.springframework.stereotype.Service
import java.time.LocalDate

/**
 * Liquida una estancia noche por noche con la tarifa de la temporada de cada noche (RN-05).
 * Los ocupantes facturables se determinan a la fecha de entrada (RN-06).
 */
@Service
class CotizarEstanciaService {
    fun cotizar(
        apartamento: Apartamento,
        estancia: Estancia,
        ocupantes: List<Ocupante>,
    ): Dinero {
        val facturables = ocupantes.count { it.esFacturable(estancia) }

        return generateSequence(estancia.periodo.inicio) { it.plusDays(1) }
            .takeWhile { it.isBefore(estancia.periodo.fin) }
            .fold(Dinero(0)) { total, noche ->
                total + tarifaDe(apartamento, noche).valor.multiplicar(facturables)
            }
    }

    private fun tarifaDe(
        apartamento: Apartamento,
        noche: LocalDate,
    ): Tarifa {
        val temporada = Temporada.clasificar(noche)

        return apartamento.tarifas.firstOrNull { it.temporada == temporada }
            ?: throw ReglaDominioException("El apartamento no tiene tarifa para la temporada $temporada")
    }
}