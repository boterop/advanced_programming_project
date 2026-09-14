package com.project.advanced.project.domain.valueobject

import kotlin.test.Test
import kotlin.test.assertEquals

class CanalOrigenTest {
    @Test
    fun deberiaDefinirLosTresCanalesDeOrigenPermitidos() {
        assertEquals(
            setOf(CanalOrigen.PORTAL, CanalOrigen.DIRECTO, CanalOrigen.EXTERNO),
            CanalOrigen.entries.toSet(),
        )
    }
}
