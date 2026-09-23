package com.project.advanced.project.domain.valueobject

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class IdentificacionApartamentoTest {

    @Test
    fun verificarIdentificadoresIguales(){
        val idUno = IdentificacionApartamento("Apto:01")
        val idDos = IdentificacionApartamento("APTO:01")

        assertEquals(idUno, idDos)
        assertEquals(idUno.hashCode(), idDos.hashCode())
    }

    @Test
    fun normalizarAMayusculas(){
        val idUno = IdentificacionApartamento("Apto:01")
        assertEquals("APTO:01", idUno.valor)
    }
}