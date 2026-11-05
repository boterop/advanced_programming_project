package com.uq.bnb.domain.valueobject;
import java.util.Map;
import java.util.Set;
public enum EstadoOperativo {
    PREPARADO,
    OCUPADO,
    PENDIENTE_PREPARACION,
    EN_PREPARACION,
    FUERA_DE_SERVICIO;
    private static final Map<EstadoOperativo, Set<EstadoOperativo>> TRANSICIONES_VALIDAS = Map.of(
            PREPARADO, Set.of(OCUPADO, FUERA_DE_SERVICIO),
            OCUPADO, Set.of(PENDIENTE_PREPARACION),
            PENDIENTE_PREPARACION, Set.of(EN_PREPARACION, FUERA_DE_SERVICIO),
            EN_PREPARACION, Set.of(PREPARADO, FUERA_DE_SERVICIO),
            FUERA_DE_SERVICIO, Set.of()
    );
    public boolean puedeTransitarA(EstadoOperativo destino) {
        return TRANSICIONES_VALIDAS.getOrDefault(this, Set.of()).contains(destino);
    }
    public boolean permiteRegistro() {
        return this == PREPARADO;
    }
}