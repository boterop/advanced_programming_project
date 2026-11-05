package com.uq.bnb.fixtures;

import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.entity.Titular;
import com.uq.bnb.domain.valueobject.CanalOrigen;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.DetalleNoche;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.VersionPolitica;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class ReservaFixture {
    private ReservaFixture() {
    }

    public static Reserva crear() {
        LocalDate inicio = LocalDate.of(2030, 6, 10);
        Titular titular = Titular.crear(TestIds.next(), "123456789", "Jhon Doe", LocalDate.of(1990, 1, 1));
        Cotizacion cotizacion = new Cotizacion(List.of(
                new DetalleNoche(inicio, "Base", Dinero.de(100_000), 1)));
        Reserva reserva = new Reserva(TestIds.next(), TestIds.next(), List.of(TestIds.next()),
                CodigoReserva.de(2030, TestIds.next().hashCode() & 0x7fffffff), "APT-001", titular,
                new Estancia(inicio, inicio.plusDays(1)), CanalOrigen.DIRECTO, cotizacion,
                new VersionPolitica(1, LocalDate.of(2030, 1, 1)), LocalDateTime.of(2030, 1, 1, 12, 0));
        reserva.agregarOcupante(titular);
        return reserva;
    }
}
