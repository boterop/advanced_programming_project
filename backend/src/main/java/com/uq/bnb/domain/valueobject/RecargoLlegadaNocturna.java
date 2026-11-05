package com.uq.bnb.domain.valueobject;
import com.uq.bnb.domain.exception.ReglaDominioException;
import java.time.LocalTime;
import java.util.Objects;
public record RecargoLlegadaNocturna(Dinero valor, LocalTime desde, LocalTime hasta) {
    public RecargoLlegadaNocturna {
        Objects.requireNonNull(valor, "El valor del recargo no puede ser nulo");
        Objects.requireNonNull(desde, "La hora de inicio de la franja nocturna no puede ser nula");
        Objects.requireNonNull(hasta, "La hora de fin de la franja nocturna no puede ser nula");
        if (valor.esCero()) {
            throw new ReglaDominioException("El recargo por llegada nocturna debe ser mayor a cero");
        }
        if (desde.equals(hasta)) {
            throw new ReglaDominioException("La franja de llegada nocturna debe tener inicio y fin distintos");
        }
    }
    public boolean aplicaA(LocalTime horaLlegada) {
        Objects.requireNonNull(horaLlegada, "La hora de llegada no puede ser nula");
        if (desde.isBefore(hasta)) {
            return !horaLlegada.isBefore(desde) && !horaLlegada.isAfter(hasta);
        }
        return !horaLlegada.isBefore(desde) || !horaLlegada.isAfter(hasta);
    }
}
