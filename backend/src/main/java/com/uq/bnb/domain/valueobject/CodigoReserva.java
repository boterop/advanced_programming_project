package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;
import java.util.regex.Pattern;

public record CodigoReserva(String valor) {
  private static final Pattern FORMATO = Pattern.compile("^RES-\\d{4}-\\d{5,}$");

  public CodigoReserva {
    if (valor == null || !FORMATO.matcher(valor).matches()) {
      throw new ReglaDominioException("El código de reserva debe tener el formato RES-AAAA-NNNNN");
    }
  }

  public static CodigoReserva de(int anio, long consecutivo) {
    return new CodigoReserva(String.format("RES-%04d-%05d", anio, consecutivo));
  }
}
