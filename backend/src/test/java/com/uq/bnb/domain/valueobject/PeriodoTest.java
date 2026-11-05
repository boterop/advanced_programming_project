package com.uq.bnb.domain.valueobject;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class PeriodoTest {
    @Test
    void elRangoDeFechasDetectaSolapamientosParcialesYCompletos() {
        LocalDate inicio = LocalDate.of(2030, 6, 10);
        Estancia base = new Estancia(inicio, inicio.plusDays(5));

        assertTrue(base.seSolapaCon(new Estancia(inicio.plusDays(1), inicio.plusDays(3))));
        assertTrue(base.seSolapaCon(new Estancia(inicio.plusDays(4), inicio.plusDays(7))));
        assertTrue(base.seSolapaCon(new Estancia(inicio.minusDays(2), inicio.plusDays(2))));
        assertFalse(base.seSolapaCon(new Estancia(inicio.plusDays(5), inicio.plusDays(7))));
        assertFalse(base.seSolapaCon(new Estancia(inicio.plusDays(7), inicio.plusDays(9))));
    }
}
