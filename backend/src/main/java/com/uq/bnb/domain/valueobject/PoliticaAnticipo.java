package com.uq.bnb.domain.valueobject;
import com.uq.bnb.domain.exception.ReglaDominioException;
import java.math.BigDecimal;
import java.util.Objects;
public record PoliticaAnticipo(boolean exige, BigDecimal porcentaje) {
    public PoliticaAnticipo {
        Objects.requireNonNull(porcentaje, "El porcentaje de anticipo no puede ser nulo");
        if (exige) {
            if (porcentaje.compareTo(BigDecimal.ZERO) <= 0 || porcentaje.compareTo(BigDecimal.ONE) > 0) {
                throw new ReglaDominioException("El porcentaje de anticipo debe estar entre 0 (exclusivo) y 1");
            }
        } else {
            porcentaje = BigDecimal.ZERO;
        }
    }
    public static PoliticaAnticipo noExige() {
        return new PoliticaAnticipo(false, BigDecimal.ZERO);
    }
    public Dinero calcularAnticipo(Dinero valorTotal) {
        Objects.requireNonNull(valorTotal, "El valor total no puede ser nulo");
        return exige ? valorTotal.multiplicar(porcentaje) : Dinero.cero();
    }
}
