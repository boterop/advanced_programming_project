package com.project.advanced.project.domain.entity

import com.project.advanced.project.domain.valueobject.Dinero
import com.project.advanced.project.domain.valueobject.FechaCreacion
import com.project.advanced.project.domain.valueobject.MedioPago

class Pago private constructor(
    val valor: Dinero,
    val medioPago: MedioPago,
    val fechaCreacion: FechaCreacion,
)
