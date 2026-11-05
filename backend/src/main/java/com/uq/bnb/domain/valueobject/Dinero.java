package com.uq.bnb.domain.valueobject;

import com.uq.bnb.domain.exception.ReglaDominioException;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class Dinero {

  private final BigDecimal monto;
  private final Moneda moneda;

  public Dinero(BigDecimal valor, Moneda moneda) {
    if (valor.compareTo(BigDecimal.ZERO) < 0) {
      throw new ReglaDominioException("El valor no puede ser negativo");
    }

    this.monto = valor;
    this.moneda = moneda;
  }

  public Dinero(Object valor, Moneda moneda) {
    this(parse(valor), moneda);
  }

  public Dinero(Object valor) {
    this(parse(valor), Moneda.COP);
  }

  public Dinero multiplicar(Object factor) {
    return new Dinero(monto.multiply(parse(factor)));
  }

  public Dinero dividir(BigDecimal divisor) {
    return new Dinero(
        monto.divide(parse(divisor), RoundingMode.HALF_UP));
  }

  public Dinero sumar(Dinero otro) {
    return new Dinero(monto.add(otro.monto));
  }

  public Dinero restar(Dinero otro) {
    return new Dinero(monto.subtract(otro.monto));
  }

  public BigDecimal cantidad() {
    return monto;
  }

  public boolean esCero() {
    return monto.compareTo(BigDecimal.ZERO) == 0;
  }

  private static BigDecimal parse(Object valor) {
    if (valor instanceof BigDecimal) {
      return (BigDecimal) valor;
    }

    if (valor instanceof Integer) {
      return BigDecimal.valueOf((Integer) valor);
    }

    if (valor instanceof Long) {
      return BigDecimal.valueOf((Long) valor);
    }

    if (valor instanceof Double) {
      return BigDecimal.valueOf((Double) valor);
    }

    throw new ReglaDominioException(
        "Tipo de valor no soportado: " +
            (valor == null ? "null" : valor.getClass().getSimpleName()));
  }

  public boolean esMayorQue(Dinero otro) {
    return this.monto.compareTo(otro.monto) > 0;
  }

  public static Dinero cero() {
    return new Dinero(BigDecimal.ZERO);
  }

  public static Dinero de(Object valor) {
    return new Dinero(valor);
  }

  public boolean esNegativo() {
    return monto.compareTo(BigDecimal.ZERO) < 0;
  }

  @Override
  public int hashCode() {
    return monto.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Dinero d
        && monto.equals(d.monto) && moneda == d.moneda;
  }
}
