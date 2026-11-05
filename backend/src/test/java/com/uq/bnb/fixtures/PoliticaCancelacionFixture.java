package com.uq.bnb.fixtures;

import com.uq.bnb.domain.entity.PoliticaCancelacion;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class PoliticaCancelacionFixture {
    private PoliticaCancelacionFixture() {
    }

    public static PoliticaCancelacion crear() {
        return PoliticaCancelacion.crear(TestIds.next(), 1, LocalDate.of(2030, 1, 1), List.of(
                new TramoAntelacion(72, BigDecimal.ZERO),
                new TramoAntelacion(0, BigDecimal.ONE)));
    }
}
