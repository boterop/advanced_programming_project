package com.project.advanced.project.domain.valueobject

import java.time.LocalDate
import java.time.Month

enum class Temporada {
    ALTA,
    MEDIA,
    BAJA,
    ;

    companion object {
        fun clasificar(fecha: LocalDate): Temporada =
            when (fecha.month) {
                Month.JANUARY -> {
                    ALTA
                }

                Month.MARCH,
                Month.APRIL,
                Month.MAY,
                -> {
                    BAJA
                }

                Month.JUNE -> {
                    if (fecha.dayOfMonth < 15) {
                        BAJA
                    } else {
                        ALTA
                    }
                }

                Month.JULY -> {
                    ALTA
                }

                Month.SEPTEMBER,
                Month.OCTOBER,
                Month.NOVEMBER,
                -> {
                    BAJA
                }

                Month.DECEMBER -> {
                    if (fecha.dayOfMonth < 15) {
                        BAJA
                    } else {
                        ALTA
                    }
                }

                else -> {
                    MEDIA
                }
            }
    }
}
