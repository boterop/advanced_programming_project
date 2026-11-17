package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import com.uq.bnb.domain.valueobject.VersionPolitica;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;

@Getter
public final class Alojamiento {

  private final ID id;
  private final String nombre;
  private final String ciudad;
  private final ParametrosAlojamiento parametros;
  private final List<Temporada> temporadas = new ArrayList<>();
  private final List<PoliticaCancelacion> historialPoliticas = new ArrayList<>();

  private Alojamiento(ID id, String nombre, String ciudad, ParametrosAlojamiento parametros) {
    this.id = id;
    this.nombre = nombre;
    this.ciudad = ciudad;
    this.parametros = parametros;
  }

  public static Alojamiento crear(
      ID id,
      ID idPoliticaBase,
      String nombre,
      String ciudad,
      ParametrosAlojamiento parametros,
      Temporada temporadaBase,
      List<TramoAntelacion> tramosPoliticaInicial,
      LocalDate vigenteDesde) {
    validarIdentidad(nombre, ciudad);
    Objects.requireNonNull(parametros, "Los parámetros del alojamiento no pueden ser nulos");
    Objects.requireNonNull(temporadaBase, "El alojamiento debe crearse con una temporada base");
    if (!temporadaBase.esBase()) {
      throw new ReglaDominioException(
          "La temporada inicial del alojamiento debe ser la temporada base");
    }
    Alojamiento alojamiento = new Alojamiento(id, nombre.trim(), ciudad.trim(), parametros);
    alojamiento.temporadas.add(temporadaBase);
    alojamiento.historialPoliticas.add(
        PoliticaCancelacion.crear(idPoliticaBase, 1, vigenteDesde, tramosPoliticaInicial));
    return alojamiento;
  }

  public static Alojamiento reconstruir(
      ID id,
      String nombre,
      String ciudad,
      ParametrosAlojamiento parametros,
      List<Temporada> temporadas,
      List<PoliticaCancelacion> politicas) {
    Alojamiento alojamiento = new Alojamiento(id, nombre, ciudad, parametros);
    alojamiento.temporadas.addAll(temporadas);
    alojamiento.historialPoliticas.addAll(politicas);
    return alojamiento;
  }

  private static void validarIdentidad(String nombre, String ciudad) {
    if (nombre == null || nombre.isBlank()) {
      throw new ReglaDominioException("El nombre del alojamiento no puede estar vacío");
    }
    if (ciudad == null || ciudad.isBlank()) {
      throw new ReglaDominioException("La ciudad del alojamiento no puede estar vacía");
    }
  }

  public void agregarTemporada(Temporada nueva) {
    Objects.requireNonNull(nueva, "La temporada no puede ser nula");
    if (nueva.esBase()) {
      throw new ReglaDominioException("Ya existe una temporada base; no puede haber una segunda");
    }
    boolean solapa = temporadas.stream().anyMatch(t -> t.seSolapaCon(nueva));
    if (solapa) {
      throw new ReglaDominioException(
          "La temporada '" + nueva.nombre() + "' se solapa con una temporada existente");
    }
    temporadas.add(nueva);
  }

  public Temporada temporadaVigente(LocalDate fecha) {
    Objects.requireNonNull(fecha, "La fecha no puede ser nula");
    return temporadas.stream()
        .filter(t -> !t.esBase())
        .filter(t -> t.cubre(fecha))
        .findFirst()
        .orElseGet(
            () ->
                temporadas.stream()
                    .filter(Temporada::esBase)
                    .findFirst()
                    .orElseThrow(
                        () ->
                            new ReglaDominioException(
                                "El alojamiento no tiene temporada base configurada")));
  }

  public Optional<Temporada> temporada(ID idTemporada) {
    return temporadas.stream().filter(t -> t.id().equals(idTemporada)).findFirst();
  }

  public PoliticaCancelacion definirNuevaPolitica(
      ID idPolitica, List<TramoAntelacion> tramos, LocalDate vigenteDesde) {
    Objects.requireNonNull(vigenteDesde, "La fecha de vigencia no puede ser nula");
    boolean noEsPosterior =
        historialPoliticas.stream().anyMatch(p -> !vigenteDesde.isAfter(p.vigenteDesde()));
    if (noEsPosterior) {
      throw new ReglaDominioException(
          "La nueva política debe regir después de la versión más reciente");
    }
    PoliticaCancelacion nueva =
        PoliticaCancelacion.crear(idPolitica, historialPoliticas.size() + 1, vigenteDesde, tramos);
    historialPoliticas.add(nueva);
    return nueva;
  }

  public PoliticaCancelacion politicaVigente(LocalDate fecha) {
    Objects.requireNonNull(fecha, "La fecha no puede ser nula");
    return historialPoliticas.stream()
        .filter(p -> !p.vigenteDesde().isAfter(fecha))
        .max(Comparator.comparing(PoliticaCancelacion::vigenteDesde))
        .orElseThrow(
            () ->
                new ReglaDominioException(
                    "No hay una política de cancelación vigente para la fecha " + fecha));
  }

  public Optional<PoliticaCancelacion> politicaPorNumero(int numero) {
    return historialPoliticas.stream()
        .filter(p -> p.numeroVersion().numero() == numero)
        .findFirst();
  }

  public Optional<PoliticaCancelacion> politicaDeVersion(VersionPolitica version) {
    Objects.requireNonNull(version, "La versión no puede ser nula");
    return historialPoliticas.stream().filter(p -> p.numeroVersion().equals(version)).findFirst();
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof Alojamiento entity && id.equals(entity.id());
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
