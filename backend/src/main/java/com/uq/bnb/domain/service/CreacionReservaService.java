package com.uq.bnb.domain.service;
import com.uq.bnb.domain.entity.Alojamiento;
import com.uq.bnb.domain.entity.Apartamento;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.entity.Reserva;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.repository.GeneradorCodigoReserva;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.ID;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.RecargoLlegadaNocturna;
import com.uq.bnb.domain.valueobject.SolicitudReserva;
import com.uq.bnb.domain.valueobject.VersionPolitica;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
public class CreacionReservaService {
    private final DisponibilidadApartamentoService disponibilidadService;
    private final CotizadorEstanciaService cotizadorService;
    private final GeneradorCodigoReserva generadorCodigo;
    private final RecargoLlegadaNocturna recargoLlegadaNocturna;
    public CreacionReservaService(DisponibilidadApartamentoService disponibilidadService,
                                  CotizadorEstanciaService cotizadorService,
                                  GeneradorCodigoReserva generadorCodigo,
                                  RecargoLlegadaNocturna recargoLlegadaNocturna) {
        this.disponibilidadService = Objects.requireNonNull(disponibilidadService, "El servicio de disponibilidad no puede ser nulo");
        this.cotizadorService = Objects.requireNonNull(cotizadorService, "El cotizador no puede ser nulo");
        this.generadorCodigo = Objects.requireNonNull(generadorCodigo, "El generador de códigos no puede ser nulo");
        this.recargoLlegadaNocturna = Objects.requireNonNull(recargoLlegadaNocturna, "El recargo por llegada nocturna no puede ser nulo");
    }
    public Reserva crear(Alojamiento alojamiento, Apartamento apartamento, SolicitudReserva solicitud, LocalDateTime ahora, Supplier<ID> ids) {
        Objects.requireNonNull(alojamiento, "El alojamiento no puede ser nulo");
        Objects.requireNonNull(apartamento, "El apartamento no puede ser nulo");
        Objects.requireNonNull(solicitud, "La solicitud no puede ser nula");
        Objects.requireNonNull(ahora, "El momento de creación no puede ser nulo");
        LocalDate hoy = ahora.toLocalDate();
        Estancia estancia = solicitud.estancia();
        if (estancia.entradaEsAnteriorA(hoy)) {
            throw new ReglaDominioException("RN-04: la fecha de entrada (" + estancia.entrada()
                    + ") no puede ser anterior a la fecha actual (" + hoy + ")");
        }
        List<Ocupante> ocupantes = new ArrayList<>();
        ocupantes.add(solicitud.titular());
        ocupantes.addAll(solicitud.acompanantes());
        long documentosDistintos = ocupantes.stream().map(Ocupante::documento).distinct().count();
        if (documentosDistintos != ocupantes.size()) {
            throw new ReglaDominioException("Un ocupante no puede aparecer dos veces en la reserva (documento repetido)");
        }
        int umbralEdad = alojamiento.parametros().umbralEdadFacturable().anios();
        if (!solicitud.titular().esFacturable(estancia.entrada(), umbralEdad)) {
            throw new ReglaDominioException("3.2: el titular debe tener " + umbralEdad
                    + " años o más a la fecha de entrada (es siempre un ocupante facturable)");
        }
        apartamento.verificarAdmiteGrupo(ocupantes.size(), solicitud.conMascota());
        if (!disponibilidadService.estaDisponible(apartamento, estancia, ocupantes.size(), solicitud.conMascota(),
                alojamiento.parametros())) {
            throw new ReglaDominioException("RN-01 / RN-07 / RN-20: el apartamento " + apartamento.identificacion()
                    + " no está disponible del " + estancia.entrada() + " al " + estancia.salida()
                    + " (tiene una reserva activa o un bloqueo en esas noches, no alcanza el tiempo de preparación"
                    + " con otra reserva, o está retirado de la venta)");
        }
        Cotizacion cotizacion = cotizadorService.cotizar(alojamiento, apartamento, estancia, ocupantes);
        VersionPolitica versionPolitica = alojamiento.politicaVigente(hoy).numeroVersion();
        ID reservaId = ids.get();
        ID folioId = ids.get();
        List<ID> idsCargos = cotizacion.desglosePorNoche().stream().map(detalle -> ids.get()).toList();
        Reserva reserva = new Reserva(reservaId, folioId, idsCargos, generadorCodigo.generar(), apartamento.identificacion(), solicitud.titular(), estancia,
                solicitud.canalOrigen(), cotizacion, versionPolitica, ahora, solicitud.conMascota());
        ocupantes.forEach(reserva::agregarOcupante);
        if (solicitud.horaEstimadaLlegada() != null) {
            reserva.registrarHoraEstimadaLlegada(ids.get(), solicitud.horaEstimadaLlegada(), recargoLlegadaNocturna);
        }
        return reserva;
    }
}
