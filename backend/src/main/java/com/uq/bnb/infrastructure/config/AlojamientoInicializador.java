package com.uq.bnb.infrastructure.config;

import com.uq.bnb.application.port.IDProvider;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Bloqueo;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.entity.Temporada;
import com.uq.bnb.domain.entity.Titular;
import com.uq.bnb.domain.repository.AlojamientoRepository;
import com.uq.bnb.domain.repository.ApartamentoRepository;
import com.uq.bnb.domain.repository.ReservaRepository;
import com.uq.bnb.domain.service.CreacionReservaService;
import com.uq.bnb.domain.service.GestionBloqueoService;
import com.uq.bnb.domain.valueobject.CanalOrigen;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.Dormitorio;
import com.uq.bnb.domain.valueobject.EstadoOperativo;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.domain.valueobject.MedioPago;
import com.uq.bnb.domain.valueobject.ParametrosAlojamiento;
import com.uq.bnb.domain.valueobject.SolicitudReserva;
import com.uq.bnb.domain.valueobject.TramoAntelacion;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AlojamientoInicializador implements ApplicationRunner {
  private static final String NOMBRE = "Búnker Orbital YoRHa - Suites & Apartamentos Temáticos";
  private static final String CIUDAD = "El Espacio";
  private static final String NOMBRE_TEMPORADA_BASE = "Base";
  private static final String NOMBRE_TEMPORADA_MEDIA = "Media (Sincronización)";
  private static final String NOMBRE_TEMPORADA_ALTA = "Alta (Despliegue de Misión)";
  private static final LocalDate MEDIA_DESDE = LocalDate.of(2026, 11, 1);
  private static final LocalDate MEDIA_HASTA = LocalDate.of(2026, 12, 15);
  private static final LocalDate ALTA_DESDE = LocalDate.of(2026, 12, 15);
  private static final LocalDate ALTA_HASTA = LocalDate.of(2027, 1, 16);
  private static final LocalDate TARIFAS_VIGENTES_DESDE = LocalDate.of(2026, 1, 1);
  private static final List<TramoAntelacion> TRAMOS_POLITICA_INICIAL =
      List.of(
          new TramoAntelacion(7 * 24, BigDecimal.ZERO),
          new TramoAntelacion(3 * 24, new BigDecimal("0.50")),
          new TramoAntelacion(0, BigDecimal.ONE));

  private record FichaApartamento(
      String id,
      String nombre,
      int dormitorios,
      int capacidad,
      long tarifaBase,
      long tarifaMedia,
      long tarifaAlta) {
    boolean esExclusive() {
      return nombre.endsWith("Exclusive");
    }
  }

  private static final List<FichaApartamento> INVENTARIO =
      List.of(
          new FichaApartamento("ATK-001", "Attack Suite", 2, 4, 80_000, 100_000, 125_000),
          new FichaApartamento(
              "ATK-002", "Attack Suite Exclusive", 4, 8, 110_000, 135_000, 165_000),
          new FichaApartamento("BTL-001", "Battle Suite", 1, 2, 90_000, 110_000, 140_000),
          new FichaApartamento(
              "BTL-002", "Battle Suite Exclusive", 3, 6, 120_000, 145_000, 180_000),
          new FichaApartamento("CTL-001", "Controller Suite", 2, 4, 95_000, 115_000, 145_000),
          new FichaApartamento(
              "CTL-002", "Controller Suite Exclusive", 4, 8, 130_000, 160_000, 200_000),
          new FichaApartamento("DFD-001", "Defender Suite", 2, 4, 85_000, 105_000, 130_000),
          new FichaApartamento(
              "DFD-002", "Defender Suite Exclusive", 4, 8, 115_000, 140_000, 170_000),
          new FichaApartamento("EXT-001", "Executioner Suite", 3, 6, 105_000, 130_000, 160_000),
          new FichaApartamento("GNR-001", "Gunner Suite", 1, 2, 85_000, 105_000, 130_000),
          new FichaApartamento(
              "GNR-002", "Gunner Suite Exclusive", 3, 6, 115_000, 140_000, 175_000),
          new FichaApartamento("HLR-001", "Healer Suite", 1, 2, 90_000, 110_000, 135_000),
          new FichaApartamento(
              "HLR-002", "Healer Suite Exclusive", 3, 6, 120_000, 145_000, 180_000),
          new FichaApartamento("OPR-001", "Operator Suite", 1, 2, 80_000, 100_000, 120_000),
          new FichaApartamento("SNR-001", "Scanner Suite", 2, 4, 100_000, 120_000, 150_000));
  private final AlojamientoRepository alojamientoRepository;
  private final ApartamentoRepository apartamentoRepository;
  private final ReservaRepository reservaRepository;
  private final CreacionReservaService creacionReservaService;
  private final GestionBloqueoService gestionBloqueoService;
  private final ParametrosAlojamiento parametros;
  private final Clock clock;
  private final IDProvider idProvider;

  public AlojamientoInicializador(
      AlojamientoRepository alojamientoRepository,
      ApartamentoRepository apartamentoRepository,
      ReservaRepository reservaRepository,
      CreacionReservaService creacionReservaService,
      GestionBloqueoService gestionBloqueoService,
      ParametrosAlojamiento parametros,
      Clock clock,
      IDProvider idProvider) {
    this.alojamientoRepository = alojamientoRepository;
    this.apartamentoRepository = apartamentoRepository;
    this.reservaRepository = reservaRepository;
    this.creacionReservaService = creacionReservaService;
    this.gestionBloqueoService = gestionBloqueoService;
    this.parametros = parametros;
    this.clock = clock;
    this.idProvider = idProvider;
  }

  @Override
  public void run(ApplicationArguments args) {
    Alojamiento alojamiento =
        alojamientoRepository
            .obtener()
            .orElseGet(
                () ->
                    Alojamiento.crear(
                        idProvider.obtener(),
                        idProvider.obtener(),
                        NOMBRE,
                        CIUDAD,
                        parametros,
                        Temporada.crearBase(idProvider.obtener(), NOMBRE_TEMPORADA_BASE),
                        TRAMOS_POLITICA_INICIAL,
                        LocalDate.now(clock)));
    agregarTemporadaSiFalta(alojamiento, NOMBRE_TEMPORADA_MEDIA, MEDIA_DESDE, MEDIA_HASTA);
    agregarTemporadaSiFalta(alojamiento, NOMBRE_TEMPORADA_ALTA, ALTA_DESDE, ALTA_HASTA);
    alojamientoRepository.guardar(alojamiento);
    cargarApartamentos(alojamiento);
    cargarBloqueoDePrueba();
    cargarReservasDePrueba(alojamiento);
  }

  private void agregarTemporadaSiFalta(
      Alojamiento alojamiento, String nombre, LocalDate desde, LocalDate hasta) {
    boolean existe = alojamiento.temporadas().stream().anyMatch(t -> t.nombre().equals(nombre));
    if (!existe) {
      alojamiento.agregarTemporada(Temporada.crear(idProvider.obtener(), nombre, desde, hasta));
    }
  }

  private void cargarApartamentos(Alojamiento alojamiento) {
    ID base = temporada(alojamiento, NOMBRE_TEMPORADA_BASE);
    ID media = temporada(alojamiento, NOMBRE_TEMPORADA_MEDIA);
    ID alta = temporada(alojamiento, NOMBRE_TEMPORADA_ALTA);
    for (FichaApartamento ficha : INVENTARIO) {
      if (apartamentoRepository.buscarPorId(ficha.id()).isPresent()) {
        continue;
      }
      Apartamento apartamento =
          Apartamento.crear(
              idProvider.obtener(),
              ficha.id(),
              ficha.nombre(),
              ficha.capacidad(),
              dormitorios(ficha),
              null,
              true,
              EstadoOperativo.PREPARADO);
      apartamento.definirTarifa(
          idProvider.obtener(), base, Dinero.de(ficha.tarifaBase()), TARIFAS_VIGENTES_DESDE);
      apartamento.definirTarifa(
          idProvider.obtener(), media, Dinero.de(ficha.tarifaMedia()), TARIFAS_VIGENTES_DESDE);
      apartamento.definirTarifa(
          idProvider.obtener(), alta, Dinero.de(ficha.tarifaAlta()), TARIFAS_VIGENTES_DESDE);
      apartamentoRepository.guardar(apartamento);
    }
  }

  private void cargarBloqueoDePrueba() {
    Apartamento apartamento = apartamentoRepository.buscarPorId("GNR-001").orElseThrow();
    if (!apartamento.bloqueos().isEmpty()) {
      return;
    }
    LocalDate hoy = LocalDate.now(clock);
    gestionBloqueoService.registrarBloqueo(
        apartamento,
        Bloqueo.crear(
            idProvider.obtener(),
            new Estancia(hoy.plusDays(10), hoy.plusDays(13)),
            "Mantenimiento preventivo de la ventilación"));
    apartamentoRepository.guardar(apartamento);
  }

  private void cargarReservasDePrueba(Alojamiento alojamiento) {
    if (!reservaRepository.buscar(null, null, null, null).isEmpty()) {
      return;
    }
    LocalDateTime ahora = LocalDateTime.now(clock);
    LocalDate hoy = ahora.toLocalDate();
    Reserva pendiente =
        crearReserva(
            alojamiento,
            "BTL-002",
            hoy.plusDays(20),
            3,
            Titular.crear(
                idProvider.obtener(), "1094000001", "Ana Torres", LocalDate.of(1990, 3, 14)),
            List.of(
                Ocupante.crear(
                    idProvider.obtener(), "1094000002", "Luis Torres", LocalDate.of(1988, 7, 2))),
            CanalOrigen.PORTAL,
            ahora);
    reservaRepository.guardar(pendiente);
    Reserva confirmada =
        crearReserva(
            alojamiento,
            "CTL-001",
            hoy.plusDays(30),
            2,
            Titular.crear(
                idProvider.obtener(), "1094000003", "María Gómez", LocalDate.of(1985, 11, 23)),
            List.of(
                Ocupante.crear(
                    idProvider.obtener(), "1094000004", "Sofía Gómez", hoy.minusYears(7))),
            CanalOrigen.DIRECTO,
            ahora);
    confirmada.confirmarConAnticipo(
        idProvider.obtener(),
        parametros.politicaAnticipo(),
        parametros.plazoConfirmacion(),
        MedioPago.TARJETA,
        "DATOS-PRUEBA-1",
        ahora);
    reservaRepository.guardar(confirmada);
    Reserva cancelada =
        crearReserva(
            alojamiento,
            "DFD-001",
            hoy.plusDays(40),
            2,
            Titular.crear(
                idProvider.obtener(), "1094000005", "Pedro Ruiz", LocalDate.of(1979, 1, 30)),
            List.of(),
            CanalOrigen.PORTAL,
            ahora);
    cancelada.cancelar(
        alojamiento.politicaDeVersion(cancelada.versionPolitica()).orElseThrow(),
        "Cambio de planes del huésped",
        ahora);
    reservaRepository.guardar(cancelada);
  }

  private Reserva crearReserva(
      Alojamiento alojamiento,
      String apartamentoId,
      LocalDate entrada,
      int noches,
      Titular titular,
      List<Ocupante> acompanantes,
      CanalOrigen canal,
      LocalDateTime ahora) {
    Apartamento apartamento = apartamentoRepository.buscarPorId(apartamentoId).orElseThrow();
    SolicitudReserva solicitud =
        new SolicitudReserva(
            titular,
            acompanantes,
            new Estancia(entrada, entrada.plusDays(noches)),
            canal,
            false,
            LocalTime.of(16, 0));
    return creacionReservaService.crear(
        alojamiento, apartamento, solicitud, ahora, idProvider::obtener);
  }

  private static ID temporada(Alojamiento alojamiento, String nombre) {
    return alojamiento.temporadas().stream()
        .filter(t -> t.nombre().equals(nombre))
        .findFirst()
        .orElseThrow()
        .id();
  }

  private static List<Dormitorio> dormitorios(FichaApartamento ficha) {
    String tipoCama = ficha.esExclusive() ? "Cama King" : "Cama doble";
    List<Dormitorio> dormitorios = new ArrayList<>();
    for (int i = 1; i <= ficha.dormitorios(); i++) {
      dormitorios.add(new Dormitorio("Dormitorio " + i, tipoCama));
    }
    return dormitorios;
  }
}
