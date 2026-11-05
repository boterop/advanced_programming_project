package com.uq.bnb.domain.entity;

import com.uq.bnb.domain.valueobject.ID;

import lombok.Getter;
import com.uq.bnb.domain.entity.Ocupante;
import com.uq.bnb.domain.exception.ReglaDominioException;
import com.uq.bnb.domain.valueobject.CanalOrigen;
import com.uq.bnb.domain.valueobject.Cancelacion;
import com.uq.bnb.domain.valueobject.CodigoReserva;
import com.uq.bnb.domain.valueobject.Cotizacion;
import com.uq.bnb.domain.valueobject.DetalleNoche;
import com.uq.bnb.domain.valueobject.Dinero;
import com.uq.bnb.domain.valueobject.EstadoReserva;
import com.uq.bnb.domain.valueobject.Estancia;
import com.uq.bnb.domain.valueobject.Gravedad;
import com.uq.bnb.domain.valueobject.HoraLimiteNoShow;
import com.uq.bnb.domain.valueobject.MedioPago;
import com.uq.bnb.domain.valueobject.PlazoConfirmacion;
import com.uq.bnb.domain.valueobject.PoliticaAnticipo;
import com.uq.bnb.domain.valueobject.RecargoLlegadaNocturna;
import com.uq.bnb.domain.valueobject.TipoCargo;
import com.uq.bnb.domain.valueobject.VersionPolitica;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Objects;


@Getter
public class Reserva {

    private final ID id;
    private final CodigoReserva codigo;
    private final String apartamentoId;
    private final Titular titular;
    private final Estancia estancia;
    private final CanalOrigen canalOrigen;
    private EstadoReserva estado;
    private final Cotizacion cotizacion;
    private final VersionPolitica versionPolitica;
    private final LocalDateTime fechaCreacion;
    private final boolean conMascota;
    private LocalTime horaEstimadaLlegada;
    private Cancelacion cancelacion;
    private final Folio folio;
    private final List<Ocupante> ocupantes;
    private final List<Novedad> novedades;


    public Reserva(ID id, ID idFolio, List<ID> idsCargos, CodigoReserva codigo, String apartamentoId, Titular titular, Estancia estancia, CanalOrigen canalOrigen,
                   Cotizacion cotizacion, VersionPolitica versionPolitica, LocalDateTime fechaCreacion) {
        this(id, idFolio, idsCargos, codigo, apartamentoId, titular, estancia, canalOrigen, cotizacion, versionPolitica, fechaCreacion, false);
    }


    public Reserva(ID id, ID idFolio, List<ID> idsCargos, CodigoReserva codigo, String apartamentoId, Titular titular, Estancia estancia, CanalOrigen canalOrigen,
                   Cotizacion cotizacion, VersionPolitica versionPolitica, LocalDateTime fechaCreacion, boolean conMascota) {
        this.id = id;
        this.codigo = Objects.requireNonNull(codigo, "El código de la reserva no puede ser nulo.");
        this.apartamentoId = Objects.requireNonNull(apartamentoId, "El ID del apartamento no puede ser nulo.");
        this.titular = Objects.requireNonNull(titular, "El titular de la reserva no puede ser nulo.");
        this.estancia = Objects.requireNonNull(estancia, "La estancia no puede ser nula.");
        this.canalOrigen = Objects.requireNonNull(canalOrigen, "El canal de origen no puede ser nulo.");
        this.cotizacion = Objects.requireNonNull(cotizacion, "La cotización no puede ser nula.");
        this.versionPolitica = Objects.requireNonNull(versionPolitica, "La versión de la política no puede ser nula.");
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación no puede ser nula.");
        this.conMascota = conMascota;
        this.horaEstimadaLlegada = null;
        this.cancelacion = null;

        this.estado = EstadoReserva.PENDIENTE;
        this.folio = Folio.crear(idFolio, this.id);
        this.ocupantes = new ArrayList<>();
        this.novedades = new ArrayList<>();

        inicializarCargosHospedaje(idsCargos);
    }

    private Reserva(ID id, CodigoReserva codigo, String apartamentoId, Titular titular, Estancia estancia,
                    CanalOrigen canalOrigen, EstadoReserva estado, Cotizacion cotizacion, VersionPolitica versionPolitica,
                    LocalDateTime fechaCreacion, boolean conMascota, LocalTime horaEstimadaLlegada,
                    Cancelacion cancelacion, Folio folio, List<Ocupante> ocupantes, List<Novedad> novedades) {
        this.id = Objects.requireNonNull(id, "El ID de la reserva no puede ser nulo.");
        this.codigo = Objects.requireNonNull(codigo, "El código de la reserva no puede ser nulo.");
        this.apartamentoId = Objects.requireNonNull(apartamentoId, "El ID del apartamento no puede ser nulo.");
        this.titular = Objects.requireNonNull(titular, "El titular no puede ser nulo.");
        this.estancia = Objects.requireNonNull(estancia, "La estancia no puede ser nula.");
        this.canalOrigen = Objects.requireNonNull(canalOrigen, "El canal de origen no puede ser nulo.");
        this.estado = Objects.requireNonNull(estado, "El estado no puede ser nulo.");
        this.cotizacion = Objects.requireNonNull(cotizacion, "La cotización no puede ser nula.");
        this.versionPolitica = Objects.requireNonNull(versionPolitica, "La versión de la política no puede ser nula.");
        this.fechaCreacion = Objects.requireNonNull(fechaCreacion, "La fecha de creación no puede ser nula.");
        this.conMascota = conMascota;
        this.horaEstimadaLlegada = horaEstimadaLlegada;
        this.cancelacion = cancelacion;
        this.folio = Objects.requireNonNull(folio, "El folio no puede ser nulo.");
        this.ocupantes = ocupantes != null ? new ArrayList<>(ocupantes) : new ArrayList<>();
        this.novedades = novedades != null ? new ArrayList<>(novedades) : new ArrayList<>();
    }


    public static Reserva reconstruir(ID id, CodigoReserva codigo, String apartamentoId, Titular titular, Estancia estancia,
                                      CanalOrigen canalOrigen, EstadoReserva estado, Cotizacion cotizacion,
                                      VersionPolitica versionPolitica, LocalDateTime fechaCreacion, boolean conMascota,
                                      LocalTime horaEstimadaLlegada, Cancelacion cancelacion, Folio folio,
                                      List<Ocupante> ocupantes, List<Novedad> novedades) {
        return new Reserva(id, codigo, apartamentoId, titular, estancia, canalOrigen, estado, cotizacion,
                versionPolitica, fechaCreacion, conMascota, horaEstimadaLlegada, cancelacion, folio, ocupantes, novedades);
    }


    private void inicializarCargosHospedaje(List<ID> idsCargos) {
        if (idsCargos == null || idsCargos.size() != cotizacion.desglosePorNoche().size()) {
            throw new ReglaDominioException("Debe recibirse un ID por cada noche cotizada.");
        }
        for (int i = 0; i < cotizacion.desglosePorNoche().size(); i++) {
            DetalleNoche detalle = cotizacion.desglosePorNoche().get(i);
            Cargo cargoNoche = Cargo.crear(idsCargos.get(i), TipoCargo.ALOJAMIENTO, detalle.subtotal());
            this.folio.agregarCargo(cargoNoche);
        }
    }


    public void registrarHoraEstimadaLlegada(ID idCargo, LocalTime hora, RecargoLlegadaNocturna recargoNocturno) {
        Objects.requireNonNull(hora, "La hora estimada de llegada no puede ser nula.");
        Objects.requireNonNull(recargoNocturno, "El recargo por llegada nocturna no puede ser nulo.");
        if (this.estado != EstadoReserva.PENDIENTE && this.estado != EstadoReserva.CONFIRMADA) {
            throw new ReglaDominioException("La hora estimada de llegada solo se registra en reservas PENDIENTE o CONFIRMADA.");
        }
        if (this.horaEstimadaLlegada != null) {
            throw new ReglaDominioException("La hora estimada de llegada ya fue registrada para esta reserva.");
        }
        this.horaEstimadaLlegada = hora;
        if (recargoNocturno.aplicaA(hora)) {
            this.folio.agregarCargo(Cargo.crear(idCargo, TipoCargo.SERVICIO_ADICIONAL, recargoNocturno.valor()));
        }
    }


    public void confirmar() {
        if (this.estado != EstadoReserva.PENDIENTE) {
            throw new ReglaDominioException("Solo se pueden confirmar reservas en estado PENDIENTE.");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }


    public void realizarCheckIn() {
        if (this.estado != EstadoReserva.CONFIRMADA) {
            throw new ReglaDominioException("Para realizar Check-in la reserva debe estar CONFIRMADA.");
        }
        if (this.ocupantes.isEmpty()) {
            throw new ReglaDominioException("Debe registrar al menos un ocupante para realizar el Check-in.");
        }
        this.estado = EstadoReserva.EN_CURSO;
    }


    public void realizarCheckOut() {
        if (this.estado != EstadoReserva.EN_CURSO) {
            throw new ReglaDominioException("Solo se puede realizar Check-out sobre reservas EN_CURSO.");
        }
        if (!this.folio.estaPazYSalvo()) {
            throw new ReglaDominioException("No se puede realizar Check-out. El folio registra un saldo pendiente de: "
                    + folio.calcularSaldoPendiente());
        }
        this.estado = EstadoReserva.FINALIZADA;
    }


    public Pago confirmarConAnticipo(ID idPago, PoliticaAnticipo politicaAnticipo, PlazoConfirmacion plazoConfirmacion,
                                     MedioPago medioPago, String referencia, LocalDateTime ahora) {
        Objects.requireNonNull(politicaAnticipo, "La política de anticipo no puede ser nula.");
        Objects.requireNonNull(plazoConfirmacion, "El plazo de confirmación no puede ser nulo.");
        Objects.requireNonNull(ahora, "El momento de la confirmación no puede ser nulo.");

        if (!this.estado.puedeTransitarA(EstadoReserva.CONFIRMADA)) {
            throw new ReglaDominioException("RN-08: solo se pueden confirmar reservas en estado PENDIENTE (estado actual: "
                    + this.estado + ").");
        }
        if (this.horaEstimadaLlegada == null) {
            throw new ReglaDominioException("RN-09: la reserva debe registrar la hora estimada de llegada antes de confirmarse.");
        }
        if (plazoConfirmacion.haVencido(this.fechaCreacion, ahora)) {
            throw new ReglaDominioException("RN-21: el plazo de confirmación de " + plazoConfirmacion.horas()
                    + " horas venció el " + this.fechaCreacion.plusHours(plazoConfirmacion.horas()) + ".");
        }
        Dinero anticipo = politicaAnticipo.calcularAnticipo(this.cotizacion.valorTotal());
        if (!anticipo.esCero() && medioPago == null) {
            throw new ReglaDominioException("Para confirmar hay que pagar el anticipo de " + anticipo.monto()
                    + " COP: indique el medio de pago.");
        }

        Pago pagoAnticipo = anticipo.esCero() ? null : this.folio.registrarPago(idPago, anticipo, medioPago, referencia, ahora.toLocalDate());
        this.estado = EstadoReserva.CONFIRMADA;
        return pagoAnticipo;
    }


    public Cancelacion cancelar(PoliticaCancelacion politica, String motivo, LocalDateTime ahora) {
        Objects.requireNonNull(politica, "La política de cancelación no puede ser nula.");
        Objects.requireNonNull(ahora, "El momento de la cancelación no puede ser nulo.");
        if (motivo == null || motivo.isBlank()) {
            throw new ReglaDominioException("El motivo de la cancelación es obligatorio.");
        }
        if (!this.estado.puedeTransitarA(EstadoReserva.CANCELADA)) {
            throw new ReglaDominioException("RN-08: no se puede cancelar una reserva en estado " + this.estado + ".");
        }
        if (!politica.numeroVersion().equals(this.versionPolitica)) {
            throw new ReglaDominioException("RN-13: la cancelación debe usar la versión " + this.versionPolitica.numero()
                    + " de la política, que es la congelada en esta reserva.");
        }

        long horasDeAntelacion = ChronoUnit.HOURS.between(ahora, this.estancia.entrada().atStartOfDay());
        BigDecimal porcentajeRetenido = politica.retencionAplicable(Math.max(0, horasDeAntelacion));
        Dinero pagado = this.folio.calcularTotalPagos();
        Dinero retenido = pagado.multiplicar(porcentajeRetenido);
        Dinero devuelto = pagado.restar(retenido);

        this.cancelacion = new Cancelacion(motivo.trim(), ahora, retenido, devuelto);
        this.estado = EstadoReserva.CANCELADA;
        return this.cancelacion;
    }


    public Pago registrarPago(ID idPago, Dinero monto, MedioPago medioPago, String referencia, LocalDate fecha) {
        if (this.estado != EstadoReserva.CONFIRMADA && this.estado != EstadoReserva.EN_CURSO) {
            throw new ReglaDominioException("RN-15: solo se registran pagos en reservas CONFIRMADA o EN_CURSO (estado actual: "
                    + this.estado + ").");
        }
        return this.folio.registrarPago(idPago, monto, medioPago, referencia, fecha);
    }


    public boolean haPasadoHoraLimiteNoShow(HoraLimiteNoShow horaLimite, LocalDateTime ahora) {
        return this.estado == EstadoReserva.CONFIRMADA && horaLimite.permiteDeclarar(this.estancia.entrada(), ahora);
    }


    public Cancelacion declararNoShow(HoraLimiteNoShow horaLimite, PoliticaCancelacion politica, LocalDateTime ahora) {
        Objects.requireNonNull(horaLimite, "La hora límite de no-show no puede ser nula.");
        Objects.requireNonNull(politica, "La política de cancelación no puede ser nula.");
        Objects.requireNonNull(ahora, "El momento de la declaración no puede ser nulo.");
        if (!this.estado.puedeTransitarA(EstadoReserva.NO_SHOW)) {
            throw new ReglaDominioException("RN-08: solo se puede declarar no-show en una reserva CONFIRMADA (estado actual: "
                    + this.estado + ").");
        }
        if (!horaLimite.permiteDeclarar(this.estancia.entrada(), ahora)) {
            throw new ReglaDominioException("RN-12: todavía no ha pasado la hora límite de no-show (" + horaLimite.hora()
                    + " del " + this.estancia.entrada() + ").");
        }
        if (!politica.numeroVersion().equals(this.versionPolitica)) {
            throw new ReglaDominioException("RN-13: el no-show debe usar la versión " + this.versionPolitica.numero()
                    + " de la política, que es la congelada en esta reserva.");
        }

        BigDecimal porcentajeRetenido = politica.retencionAplicable(0);
        Dinero pagado = this.folio.calcularTotalPagos();
        Dinero retenido = pagado.multiplicar(porcentajeRetenido);
        Dinero devuelto = pagado.restar(retenido);

        this.cancelacion = new Cancelacion("NO_SHOW: el titular no se presentó antes de las " + horaLimite.hora()
                + " del día de entrada", ahora, retenido, devuelto);
        this.estado = EstadoReserva.NO_SHOW;
        return this.cancelacion;
    }


    public boolean haVencidoPlazoConfirmacion(PlazoConfirmacion plazoConfirmacion, LocalDateTime ahora) {
        return this.estado == EstadoReserva.PENDIENTE && plazoConfirmacion.haVencido(this.fechaCreacion, ahora);
    }


    public Cancelacion cancelarPorVencimiento(PlazoConfirmacion plazoConfirmacion, LocalDateTime ahora) {
        if (!haVencidoPlazoConfirmacion(plazoConfirmacion, ahora)) {
            throw new ReglaDominioException("RN-21: la reserva " + this.codigo.valor()
                    + " no está PENDIENTE con el plazo de confirmación vencido.");
        }
        Dinero pagado = this.folio.calcularTotalPagos();
        this.cancelacion = new Cancelacion("RN-21: plazo de confirmación de " + plazoConfirmacion.horas() + " horas vencido",
                ahora, Dinero.cero(), pagado);
        this.estado = EstadoReserva.CANCELADA;
        return this.cancelacion;
    }


    public void agregarOcupante(Ocupante ocupante) {
        Objects.requireNonNull(ocupante, "El ocupante no puede ser nulo.");
        this.ocupantes.add(ocupante);
    }


    public void registrarNovedad(ID idNovedad, LocalDate fecha, String autor, String descripcion, Gravedad gravedad) {
        Novedad novedad = Novedad.crear(idNovedad, fecha, autor, descripcion, gravedad);
        this.novedades.add(novedad);
    }













    public int cuposNecesarios() {
        return ocupantes.size() + (conMascota ? 1 : 0);
    }



    @Override
    public boolean equals(Object other) {
        return other instanceof Reserva entity && id.equals(entity.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
