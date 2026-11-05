package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.EstadoOperativo;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.domain.valueobject.TipoApartamento;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;

@Getter
public final class Apartamento {

  private final ID id;
  private final String identificacion;
  private final String nombre;
  private int capacidad;
  private final List<Dormitorio> dormitorios;
  private final TipoApartamento tipo;
  private final boolean permiteMascotas;
  private EstadoOperativo estadoOperativo;
  private final List<Tarifa> tarifas = new ArrayList<>();
  private final List<Bloqueo> bloqueos = new ArrayList<>();
  private final List<Novedad> novedades = new ArrayList<>();
  private boolean activo;

  private Apartamento(
      ID id,
      String identificacion,
      String nombre,
      int capacidad,
      List<Dormitorio> dormitorios,
      TipoApartamento tipo,
      boolean permiteMascotas,
      EstadoOperativo estadoOperativo) {
    this.id = id;
    this.identificacion = identificacion;
    this.nombre = nombre;
    this.capacidad = capacidad;
    this.dormitorios = new ArrayList<>(dormitorios);
    this.tipo = tipo;
    this.permiteMascotas = permiteMascotas;
    this.estadoOperativo = estadoOperativo;
    this.activo = true;
  }

  public static Apartamento crear(
      ID id,
      String identificacion,
      String nombre,
      int capacidad,
      List<Dormitorio> dormitorios,
      EstadoOperativo estadoOperativoInicial) {
    return crear(id, identificacion, nombre, capacidad, dormitorios, null, estadoOperativoInicial);
  }

  public static Apartamento crear(
      ID id,
      String identificacion,
      String nombre,
      int capacidad,
      List<Dormitorio> dormitorios,
      TipoApartamento tipo,
      EstadoOperativo estadoOperativoInicial) {
    return crear(
        id, identificacion, nombre, capacidad, dormitorios, tipo, false, estadoOperativoInicial);
  }

  public static Apartamento crear(
      ID id,
      String identificacion,
      String nombre,
      int capacidad,
      List<Dormitorio> dormitorios,
      TipoApartamento tipo,
      boolean permiteMascotas,
      EstadoOperativo estadoOperativoInicial) {
    if (identificacion == null || identificacion.isBlank()) {
      throw new ReglaDominioException("La identificación del apartamento no puede estar vacía");
    }
    if (nombre == null || nombre.isBlank()) {
      throw new ReglaDominioException("El nombre del apartamento no puede estar vacío");
    }
    if (capacidad <= 0) {
      throw new ReglaDominioException("La capacidad del apartamento debe ser mayor a cero");
    }
    Objects.requireNonNull(dormitorios, "La lista de dormitorios no puede ser nula");
    if (dormitorios.isEmpty()) {
      throw new ReglaDominioException("Un apartamento debe tener al menos un dormitorio");
    }
    Objects.requireNonNull(estadoOperativoInicial, "El estado operativo inicial no puede ser nulo");
    return new Apartamento(
        id,
        identificacion,
        nombre,
        capacidad,
        dormitorios,
        tipo,
        permiteMascotas,
        estadoOperativoInicial);
  }

  public static Apartamento reconstruir(
      ID id,
      String identificacion,
      String nombre,
      int capacidad,
      List<Dormitorio> dormitorios,
      TipoApartamento tipo,
      boolean permiteMascotas,
      EstadoOperativo estadoOperativo,
      List<Tarifa> tarifas,
      List<Bloqueo> bloqueos,
      List<Novedad> novedades,
      boolean activo) {
    Apartamento apartamento =
        new Apartamento(
            id,
            identificacion,
            nombre,
            capacidad,
            dormitorios,
            tipo,
            permiteMascotas,
            estadoOperativo);
    apartamento.tarifas.addAll(tarifas);
    apartamento.bloqueos.addAll(bloqueos);
    apartamento.novedades.addAll(novedades);
    apartamento.activo = activo;
    return apartamento;
  }

  public void registrarOcupacion() {
    transitarA(EstadoOperativo.OCUPADO);
  }

  public void registrarDesocupacion() {
    transitarA(EstadoOperativo.PENDIENTE_PREPARACION);
  }

  public void iniciarPreparacion() {
    transitarA(EstadoOperativo.EN_PREPARACION);
  }

  public void marcarPreparado() {
    transitarA(EstadoOperativo.PREPARADO);
  }

  public void sacarDeServicio() {
    transitarA(EstadoOperativo.FUERA_DE_SERVICIO);
  }

  private void transitarA(EstadoOperativo destino) {
    if (!this.estadoOperativo.puedeTransitarA(destino)) {
      throw new ReglaDominioException(
          "Transición de estado operativo inválida: " + this.estadoOperativo + " -> " + destino);
    }
    this.estadoOperativo = destino;
  }

  public boolean admiteGrupo(int totalOcupantes, boolean conMascota) {
    if (conMascota && !permiteMascotas) {
      return false;
    }
    return cuposNecesarios(totalOcupantes, conMascota) <= capacidad;
  }

  public void verificarAdmiteGrupo(int totalOcupantes, boolean conMascota) {
    if (conMascota && !permiteMascotas) {
      throw new ReglaDominioException(
          "RN-Propia-03: el apartamento " + identificacion + " no permite mascotas");
    }
    int cupos = cuposNecesarios(totalOcupantes, conMascota);
    if (cupos > capacidad) {
      throw new ReglaDominioException(
          conMascota
              ? "RN-02 / RN-Propia-03: "
                  + totalOcupantes
                  + " ocupantes con mascota necesitan "
                  + cupos
                  + " cupos y el apartamento "
                  + identificacion
                  + " solo tiene "
                  + capacidad
              : "RN-02: "
                  + totalOcupantes
                  + " ocupantes superan la capacidad de "
                  + capacidad
                  + " del apartamento "
                  + identificacion);
    }
  }

  private static int cuposNecesarios(int totalOcupantes, boolean conMascota) {
    return totalOcupantes + (conMascota ? 1 : 0);
  }

  public boolean estaDisponible(
      Estancia estanciaSolicitada,
      int totalOcupantes,
      boolean conMascota,
      List<Estancia> estanciasReservasActivas,
      boolean permiteRecambioMismoDia) {
    Objects.requireNonNull(estanciaSolicitada, "La estancia solicitada no puede ser nula");
    Objects.requireNonNull(
        estanciasReservasActivas, "La lista de estancias reservadas no puede ser nula");

    if (!activo) {
      return false;
    }
    if (!admiteGrupo(totalOcupantes, conMascota)) {
      return false;
    }
    boolean bloqueado = bloqueos.stream().anyMatch(b -> b.seSolapaCon(estanciaSolicitada));
    if (bloqueado) {
      return false;
    }
    return estanciasReservasActivas.stream()
        .noneMatch(
            e ->
                e.seSolapaCon(estanciaSolicitada)
                    || (!permiteRecambioMismoDia && e.esContiguaA(estanciaSolicitada)));
  }

  public void agregarBloqueo(Bloqueo nuevo) {
    Objects.requireNonNull(nuevo, "El bloqueo no puede ser nulo");
    boolean solapaConOtroBloqueo = bloqueos.stream().anyMatch(b -> b.seSolapaCon(nuevo.rango()));
    if (solapaConOtroBloqueo) {
      throw new ReglaDominioException(
          "El nuevo bloqueo se solapa con un bloqueo existente de este apartamento");
    }
    bloqueos.add(nuevo);
  }

  public boolean eliminarBloqueo(ID idBloqueo) {
    return bloqueos.removeIf(bloqueo -> bloqueo.id().equals(idBloqueo));
  }

  public void agregarTarifa(Tarifa nueva) {
    Objects.requireNonNull(nueva, "La tarifa no puede ser nula");
    tarifas.add(nueva);
  }

  public Tarifa definirTarifa(
      ID idTarifa, ID temporadaId, Dinero valorPorNoche, LocalDate vigenteDesde) {
    Objects.requireNonNull(temporadaId, "El identificador de temporada no puede ser nulo");
    Objects.requireNonNull(vigenteDesde, "La fecha de vigencia no puede ser nula");
    boolean noEsPosterior =
        tarifas.stream()
            .filter(t -> t.temporadaId().equals(temporadaId))
            .anyMatch(t -> !vigenteDesde.isAfter(t.vigenteDesde()));
    if (noEsPosterior) {
      throw new ReglaDominioException(
          "La nueva tarifa del apartamento "
              + identificacion
              + " debe regir después de la tarifa más reciente de esa temporada");
    }
    Tarifa nueva = Tarifa.crear(idTarifa, temporadaId, valorPorNoche, vigenteDesde);
    tarifas.add(nueva);
    return nueva;
  }

  public List<Tarifa> historialTarifas(ID temporadaId) {
    Objects.requireNonNull(temporadaId, "El identificador de temporada no puede ser nulo");
    return tarifas.stream()
        .filter(t -> t.temporadaId().equals(temporadaId))
        .sorted(Comparator.comparing(Tarifa::vigenteDesde).reversed())
        .toList();
  }

  public List<Tarifa> historialTarifas() {
    return tarifas.stream().sorted(Comparator.comparing(Tarifa::vigenteDesde).reversed()).toList();
  }

  public List<ID> temporadasSinTarifa(Collection<ID> temporadaIds) {
    Objects.requireNonNull(temporadaIds, "Las temporadas no pueden ser nulas");
    return temporadaIds.stream()
        .filter(id -> tarifas.stream().noneMatch(t -> t.temporadaId().equals(id)))
        .toList();
  }

  public Tarifa tarifaVigente(ID temporadaId, LocalDate fecha) {
    Objects.requireNonNull(temporadaId, "El identificador de temporada no puede ser nulo");
    Objects.requireNonNull(fecha, "La fecha no puede ser nula");
    return tarifas.stream()
        .filter(t -> t.temporadaId().equals(temporadaId))
        .filter(t -> !t.vigenteDesde().isAfter(fecha))
        .max(Comparator.comparing(Tarifa::vigenteDesde))
        .orElseThrow(
            () ->
                new ReglaDominioException(
                    "El apartamento "
                        + identificacion
                        + " no tiene tarifa vigente para la temporada indicada en la fecha "
                        + fecha));
  }

  public Optional<Tarifa> tarifa(ID idTarifa) {
    Objects.requireNonNull(idTarifa, "El identificador de la tarifa no puede ser nulo");
    return tarifas.stream().filter(t -> t.id().equals(idTarifa)).findFirst();
  }

  public Optional<Bloqueo> bloqueo(ID idBloqueo) {
    Objects.requireNonNull(idBloqueo, "El identificador del bloqueo no puede ser nulo");
    return bloqueos.stream().filter(b -> b.id().equals(idBloqueo)).findFirst();
  }

  public void cambiarCapacidad(int nuevaCapacidad) {
    if (!activo) {
      throw new ReglaDominioException(
          "El apartamento "
              + identificacion
              + " está retirado de la venta: no se puede cambiar su capacidad");
    }
    if (nuevaCapacidad <= 0) {
      throw new ReglaDominioException("La capacidad del apartamento debe ser mayor a cero");
    }
    this.capacidad = nuevaCapacidad;
  }

  public void registrarNovedad(Novedad novedad) {
    Objects.requireNonNull(novedad, "La novedad no puede ser nula");
    novedades.add(novedad);
  }

  public void desactivar() {
    if (!activo) {
      throw new ReglaDominioException(
          "El apartamento " + identificacion + " ya está retirado de la venta");
    }
    this.activo = false;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Apartamento entity && id.equals(entity.id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
