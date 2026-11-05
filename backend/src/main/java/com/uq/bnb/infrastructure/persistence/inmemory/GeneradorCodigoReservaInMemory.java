package com.uq.bnb.infrastructure.persistence.inmemory;
import com.uq.bnb.domain.repository.GeneradorCodigoReserva;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import java.util.concurrent.atomic.AtomicLong;
public class GeneradorCodigoReservaInMemory implements GeneradorCodigoReserva {
    private final int anio;
    private final AtomicLong consecutivo = new AtomicLong();
    public GeneradorCodigoReservaInMemory(int anio) {
        this.anio = anio;
    }
    @Override
    public CodigoReserva generar() {
        return CodigoReserva.de(anio, consecutivo.incrementAndGet());
    }
}
