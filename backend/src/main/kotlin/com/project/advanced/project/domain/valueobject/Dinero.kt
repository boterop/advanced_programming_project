package com.project.advanced.project.domain.valueobject

import com.project.advanced.project.domain.exception.ReglaDominioException
import java.math.BigDecimal
import java.math.RoundingMode

data class Dinero(
    private val valor: BigDecimal,
) {
    constructor(valor: Any) : this(parse(valor))

    init {
        if (valor < BigDecimal.ZERO) {
            throw ReglaDominioException("El valor no puede ser negativo")
        }
    }

    fun multiplicar(factor: Any): Dinero = Dinero(valor.multiply(parse(factor)))

    fun dividir(divisor: BigDecimal): Dinero = Dinero(valor.divide(parse(divisor), RoundingMode.HALF_UP))

    fun sumar(otro: Dinero): Dinero = Dinero(valor.add(otro.valor))

    fun restar(otro: Dinero): Dinero = Dinero(valor.subtract(otro.valor))

    fun cantidad(): BigDecimal = valor

    private companion object {
        fun parse(valor: Any): BigDecimal =
            when (valor) {
                is BigDecimal -> valor

                is Int -> valor.toBigDecimal()

                is Long -> valor.toBigDecimal()

                is Double -> BigDecimal.valueOf(valor)

                else -> throw ReglaDominioException(
                    "Tipo de valor no soportado: ${valor::class.simpleName}",
                )
            }
    }

    override fun hashCode(): Int = valor.hashCode()

    override fun equals(other: Any?): Boolean = other is Dinero && valor == other.valor
}
