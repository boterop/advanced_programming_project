package com.uq.bnb.domain.valueobject;
import com.uq.bnb.domain.exception.ReglaDominioException;
import java.time.LocalDate;
import java.util.Objects;
public record DetalleNoche(LocalDate fecha, String nombreTemporada, Dinero tarifaAplicada, int ocupantesFacturables) {
    public DetalleNoche {
        Objects.requireNonNull(fecha, "La fecha no puede ser nula");
        if (nombreTemporada == null || nombreTemporada.isBlank()) {
            throw new ReglaDominioException("El nombre de la temporada no puede estar vacío");
        }
        Objects.requireNonNull(tarifaAplicada, "La tarifa aplicada no puede ser nula");
        if (tarifaAplicada.esNegativo()) {
            throw new ReglaDominioException("La tarifa aplicada no puede ser negativa");
        }
        if (ocupantesFacturables < 0) {
            throw new ReglaDominioException("El número de ocupantes facturables no puede ser negativo");
        }
    }
    public Dinero subtotal() {
        return tarifaAplicada.multiplicar(ocupantesFacturables);
    }
}