package com.uq.bnb.fixtures;

import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.valueobject.Estancia;
import java.time.LocalDate;

public final class BloqueoFixture {
    private BloqueoFixture() {
    }

    public static Bloqueo crear() {
        LocalDate inicio = LocalDate.of(2030, 6, 10);
        return Bloqueo.crear(TestIds.next(), new Estancia(inicio, inicio.plusDays(3)), "Uso interno");
    }
}
