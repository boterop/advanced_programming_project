package com.uq.bnb.domain.valueobject;
import java.util.Map;
import java.util.Set;
public enum EstadoReserva {
    PENDIENTE,
    CONFIRMADA,
    EN_CURSO,
    FINALIZADA,
    CANCELADA,
    NO_SHOW;
    private static final Map<EstadoReserva, Set<EstadoReserva>> TRANSICIONES_VALIDAS = Map.of(
            PENDIENTE, Set.of(CONFIRMADA, CANCELADA),
            CONFIRMADA, Set.of(EN_CURSO, CANCELADA, NO_SHOW),
            EN_CURSO, Set.of(FINALIZADA)
    );
    public boolean puedeTransitarA(EstadoReserva destino) {
        return TRANSICIONES_VALIDAS.getOrDefault(this, Set.of()).contains(destino);
    }
    public boolean esActiva() {
        return this == PENDIENTE || this == CONFIRMADA || this == EN_CURSO;
    }
    public boolean esTerminal() {
        return this == FINALIZADA || this == CANCELADA || this == NO_SHOW;
    }
}