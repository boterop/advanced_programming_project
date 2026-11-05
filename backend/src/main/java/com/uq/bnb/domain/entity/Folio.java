package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.domain.valueobject.MedioPago;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;

@Getter
public class Folio {

  private final ID id;
  private final ID reservaId;
  private final List<Cargo> cargos;
  private final List<Pago> pagos;

  private Folio(ID id, ID reservaId, List<Cargo> cargos, List<Pago> pagos) {
    this.id = Objects.requireNonNull(id, "El ID del folio no puede ser nulo.");
    this.reservaId = Objects.requireNonNull(reservaId, "El ID de la reserva no puede ser nulo.");
    this.cargos = cargos != null ? new ArrayList<>(cargos) : new ArrayList<>();
    this.pagos = pagos != null ? new ArrayList<>(pagos) : new ArrayList<>();
  }

  public static Folio crear(ID id, ID reservaId) {
    return new Folio(id, reservaId, List.of(), List.of());
  }

  public static Folio reconstruir(ID id, ID reservaId, List<Cargo> cargos, List<Pago> pagos) {
    return new Folio(id, reservaId, cargos, pagos);
  }

  public void agregarCargo(Cargo cargo) {
    Objects.requireNonNull(cargo, "El cargo a agregar no puede ser nulo.");
    this.cargos.add(cargo);
  }

  public Pago registrarPago(ID idPago, Dinero monto, MedioPago medioPago, String referencia) {
    return registrarPago(idPago, monto, medioPago, referencia, LocalDate.now());
  }

  public Pago registrarPago(
      ID idPago, Dinero monto, MedioPago medioPago, String referencia, LocalDate fecha) {
    Objects.requireNonNull(monto, "El monto del pago no puede ser nulo.");
    Objects.requireNonNull(medioPago, "El medio de pago no puede ser nulo.");

    if (monto.esNegativo() || monto.esCero()) {
      throw new ReglaDominioException("El monto del pago debe ser mayor a cero.");
    }
    Dinero saldoPendiente = calcularSaldoPendiente();
    if (monto.esMayorQue(saldoPendiente)) {
      throw new ReglaDominioException(
          "El pago de "
              + monto.monto()
              + " supera el saldo pendiente del folio ("
              + saldoPendiente.monto()
              + ").");
    }

    Pago nuevoPago = Pago.crear(idPago, medioPago, fecha, monto);
    this.pagos.add(nuevoPago);
    return nuevoPago;
  }

  public Dinero calcularTotalCargos() {
    return cargos.stream().map(Cargo::valor).reduce(Dinero.cero(), Dinero::sumar);
  }

  public Dinero calcularTotalPagos() {
    return pagos.stream().map(Pago::valor).reduce(Dinero.cero(), Dinero::sumar);
  }

  public Dinero calcularSaldoPendiente() {
    return calcularTotalCargos().restar(calcularTotalPagos());
  }

  public boolean estaPazYSalvo() {
    Dinero saldo = calcularSaldoPendiente();
    return saldo.esNegativo() || saldo.esCero();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Folio entity && id.equals(entity.id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
