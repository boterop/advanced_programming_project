package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record Estancia(LocalDate entrada, LocalDate salida) {
  public Estancia {
    Objects.requireNonNull(entrada, "La fecha de entrada no puede ser nula");
    Objects.requireNonNull(salida, "La fecha de salida no puede ser nula");
    if (!salida.isAfter(entrada)) {
      throw new ReglaDominioException(
          "RN-03: la fecha de salida ("
              + salida
              + ") debe ser posterior a la fecha de entrada ("
              + entrada
              + ")");
    }
  }

  public long noches() {
    return ChronoUnit.DAYS.between(entrada, salida);
  }

  public boolean seSolapaCon(Estancia otra) {
    Objects.requireNonNull(otra, "La estancia a comparar no puede ser nula");
    return this.entrada.isBefore(otra.salida) && otra.entrada.isBefore(this.salida);
  }

  public boolean esContiguaA(Estancia otra) {
    Objects.requireNonNull(otra, "La estancia a comparar no puede ser nula");
    return this.salida.equals(otra.entrada) || otra.salida.equals(this.entrada);
  }

  public List<LocalDate> nochesOcupadas() {
    List<LocalDate> resultado = new ArrayList<>();
    for (LocalDate noche = entrada; noche.isBefore(salida); noche = noche.plusDays(1)) {
      resultado.add(noche);
    }
    return List.copyOf(resultado);
  }

  public boolean incluyeFecha(LocalDate fecha) {
    Objects.requireNonNull(fecha, "La fecha no puede ser nula");
    return !fecha.isBefore(entrada) && fecha.isBefore(salida);
  }

  public boolean entradaEsAnteriorA(LocalDate fechaReferencia) {
    Objects.requireNonNull(fechaReferencia, "La fecha de referencia no puede ser nula");
    return this.entrada.isBefore(fechaReferencia);
  }
}
